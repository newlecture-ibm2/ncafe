import { NextRequest, NextResponse } from 'next/server';
import { getSession } from '@/app/lib/session';

const API_BASE = process.env.API_BASE_URL || 'http://localhost:8081';

export async function POST(req: NextRequest) {
    try {
        const body = await req.json();

        // 1. Spring Boot 로그인 API 호출 (서버 → 서버)
        const loginRes = await fetch(`${API_BASE}/api/auth/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(body),
        });

        if (!loginRes.ok) {
            const error = await loginRes.json().catch(() => ({ error: '로그인에 실패했습니다.' }));
            return NextResponse.json(error, { status: loginRes.status });
        }

        const data = await loginRes.json();
        const token = data.token;

        if (!token) {
            return NextResponse.json({ error: '토큰을 받지 못했습니다.' }, { status: 500 });
        }

        // 2. 세션에 저장 (httpOnly 쿠키로 암호화되어 저장됨)
        const session = await getSession();
        session.token = token;
        session.user = {
            username: data.username,
            roles: data.roles,
        };
        await session.save();

        // 3. 클라이언트에 user 정보만 반환 (JWT는 절대 반환하지 않음!)
        return NextResponse.json({ user: session.user });
    } catch (error) {
        console.error('Login error:', error);
        return NextResponse.json({ error: 'Internal Server Error' }, { status: 500 });
    }
}
