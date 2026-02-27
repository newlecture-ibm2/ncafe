import { NextRequest, NextResponse } from 'next/server';

// 로그인 필요한 경로
const PROTECTED_PATHS = ['/admin'];

// 세션 쿠키 이름 (session.ts의 cookieName과 동일해야 함)
const SESSION_COOKIE_NAME = 'ncafe_session_v1';

export function middleware(req: NextRequest) {
    const { pathname } = req.nextUrl;

    console.log("middleware.ts pathname : ", pathname);
    // 보호 경로인지 확인
    const isProtected = PROTECTED_PATHS.some((path) => pathname.startsWith(path));

    if (isProtected) {
        // 세션 쿠키 존재 여부 확인
        const sessionCookie = req.cookies.get(SESSION_COOKIE_NAME);
        if (!sessionCookie) {
            const loginUrl = new URL('/login', req.url);
            loginUrl.searchParams.set('redirect', pathname);
            return NextResponse.redirect(loginUrl);
        }
    }

    return NextResponse.next();
}

// 매칭 설정
export const config = {
    matcher: ['/admin/:path*'],
};
