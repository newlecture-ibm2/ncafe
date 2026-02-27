import { getIronSession, SessionOptions } from 'iron-session';
import { cookies } from 'next/headers';

// ──────────────────────────────────────
// 세션에 저장할 사용자 정보 타입
// Spring Boot /api/auth/me 응답에 맞게 수정
// ──────────────────────────────────────
export interface SessionUser {
    username: string;
    roles: string[];
}

export interface SessionData {
    token: string;      // Spring Boot에서 발급받은 JWT
    user: SessionUser;  // 사용자 정보
}

export const sessionOptions: SessionOptions = {
    password: process.env.SESSION_SECRET || 'default-secret-change-in-production-32-chars-min',
    cookieName: 'ncafe_session_v1',
    cookieOptions: {
        httpOnly: true,                                   // JavaScript 접근 차단
        secure: process.env.NODE_ENV === 'production',    // 운영에서만 HTTPS 필수
        sameSite: 'lax' as const,                         // CSRF 기본 방어
        path: '/',
        maxAge: 60 * 60 * 24,                             // 24시간 (JWT 만료시간과 맞추기)
    },
};

export async function getSession() {
    const cookieStore = await cookies();
    return getIronSession<SessionData>(cookieStore, sessionOptions);
}
