/**
 * 인증 상태 관리 스토어 (Zustand) - JWT 방식
 *
 * 흐름:
 * 1. 로그인: POST /api/auth/login (BFF) → Spring Boot 연동 및세션 쿠키(httpOnly) 설정
 * 2. 토큰 저장: 브라우저 쿠키에 암호화된 세션 자동 저장 (JS 접근 불가)
 * 3. 상태 복원: GET /api/auth/session → BFF 세션에서 사용자 정보 조회
 * 4. 로그아웃: POST /api/auth/logout → 세션 쿠키 삭제 + 상태 초기화
 */

import { create } from 'zustand';

export interface AuthUser {
    username: string;
    roles: string[];
}

interface AuthState {
    user: AuthUser | null;
    loading: boolean;

    // 액션
    setUser: (user: AuthUser | null) => void;
    login: (username: string, password: string) => Promise<void>;
    logout: () => void;
    fetchMe: () => Promise<void>;
}

const API_URL = ''; // Relative path because of BFF proxy

export const useAuthStore = create<AuthState>((set) => ({
    user: null,
    loading: true,

    setUser: (user) => set({ user }),

    /** BFF 로그인 (세션 쿠키 발급) */
    login: async (username, password) => {
        try {
            const response = await fetch(`${API_URL}/api/auth/login`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });

            if (!response.ok) {
                const errorData = await response.json().catch(() => ({}));
                throw new Error(errorData.error || '아이디 또는 비밀번호가 올바르지 않습니다.');
            }

            // BFF 로그인 성공: 서버 세션 쿠키가 이미 설정됨
            const data = await response.json();

            // 사용자 정보 설정
            set({ user: data.user, loading: false });
        } catch (error) {
            set({ user: null, loading: false });
            throw error;
        }
    },

    /** BFF 세션 정보 조회 */
    fetchMe: async () => {
        set({ loading: true });
        try {
            const res = await fetch(`${API_URL}/api/auth/session`);

            if (res.ok) {
                const data = await res.json();
                set({ user: data.user, loading: false });
            } else {
                set({ user: null, loading: false });
            }
        } catch {
            set({ user: null, loading: false });
        }
    },

    /** 로그아웃: BFF 세션 쿠키 삭제 */
    logout: async () => {
        await fetch(`${API_URL}/api/auth/logout`, { method: 'POST' });
        set({ user: null });
        window.location.href = '/login';
    },
}));

