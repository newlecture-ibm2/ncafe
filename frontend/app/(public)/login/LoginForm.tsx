// app/login/LoginForm.tsx
'use client';

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import { useAuthStore } from '@/store/authStore';
import styles from './login.module.css';

export default function LoginForm() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);

    const login = useAuthStore((state) => state.login);
    const router = useRouter();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setLoading(true);

        try {
            await login(username, password);
            router.push('/');          // 로그인 성공 → 홈으로
            router.refresh();
        } catch (err: unknown) {
            const message = err instanceof Error
                ? err.message
                : '로그인 중 오류가 발생했습니다.';
            setError(message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} className={styles.loginCard}>
            <h1 className={styles.title}>로그인</h1>

            <div className={styles.formGroup}>
                <label htmlFor="username" className={styles.label}>아이디</label>
                <input
                    id="username"
                    type="text"
                    className={styles.input}
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    placeholder="아이디를 입력하세요"
                    required
                />
            </div>

            <div className={styles.formGroup}>
                <label htmlFor="password" className={styles.label}>비밀번호</label>
                <input
                    id="password"
                    type="password"
                    className={styles.input}
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    placeholder="비밀번호를 입력하세요"
                    required
                />
            </div>

            {error && <p className={styles.errorMessage}>{error}</p>}

            <button type="submit" className={styles.button} disabled={loading}>
                {loading ? '로그인 중...' : '로그인'}
            </button>
        </form>
    );
}
