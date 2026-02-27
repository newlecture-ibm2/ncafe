import { NextRequest, NextResponse } from 'next/server';
import { getSession } from '@/app/lib/session';

const API_BASE = process.env.API_BASE_URL || 'http://localhost:8081';

async function proxyRequest(req: NextRequest) {
    const session = await getSession();

    // URL 구성
    const url = new URL(req.url);
    const path = url.pathname;   // 예: /api/menus
    const search = url.search;     // 예: ?page=0
    const targetUrl = `${API_BASE}${path}${search}`;

    // 요청 헤더 구성
    const headers = new Headers();

    // 클라이언트의 모든 헤더 복사 (일부 제외)
    req.headers.forEach((value, key) => {
        if (key.toLowerCase() !== 'host' && key.toLowerCase() !== 'cookie') {
            headers.set(key, value);
        }
    });

    // ★ 핵심: 세션에 JWT가 있으면 Authorization 헤더 주입
    if (session.token) {
        headers.set('Authorization', `Bearer ${session.token}`);
    }

    // 요청 본문 전달
    let body: BodyInit | null = null;
    if (req.method !== 'GET' && req.method !== 'HEAD') {
        const contentType = req.headers.get('content-type');
        if (contentType?.includes('multipart/form-data')) {
            body = await req.blob();
        } else {
            body = await req.text();
        }
    }

    try {
        const proxyRes = await fetch(targetUrl, {
            method: req.method,
            headers,
            body,
            cache: 'no-store'
        });

        // 401 응답 시 세션 삭제 (토큰 만료)
        if (proxyRes.status === 401 && session.token) {
            session.destroy();
        }

        // 응답 구성
        const responseHeaders = new Headers();
        proxyRes.headers.forEach((value, key) => {
            // 보안 헤더 등 필요한 것만 전달하거나 전부 전달
            if (key.toLowerCase() === 'content-type') {
                responseHeaders.set(key, value);
            }
        });

        return new NextResponse(proxyRes.body, {
            status: proxyRes.status,
            statusText: proxyRes.statusText,
            headers: responseHeaders,
        });
    } catch (error) {
        console.error('Proxy error:', error);
        return NextResponse.json({ error: 'Backend connection failed' }, { status: 502 });
    }
}

export const GET = proxyRequest;
export const POST = proxyRequest;
export const PUT = proxyRequest;
export const DELETE = proxyRequest;
export const PATCH = proxyRequest;
