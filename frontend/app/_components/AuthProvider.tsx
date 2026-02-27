'use client';

/**
 * AuthProvider
 * 앱 최초 로드 시 /api/auth/me를 호출하여 기존 세션(JSESSIONID 쿠키)으로
 * 로그인 상태를 자동 복원합니다.
 */

import { useEffect } from 'react';
import { useAuthStore } from '@/store/authStore';

export default function AuthProvider({ children }: { children: React.ReactNode }) {
    const fetchMe = useAuthStore((state) => state.fetchMe);

    useEffect(() => {
        fetchMe();
    }, [fetchMe]);

    return <>{children}</>;
}
