// app/login/LoginForm.tsx
'use client';

import { useState } from 'react';
import styles from './login.module.css';

export default function LoginForm() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        try {
            // API_URL 환경변수 사용 (없으면 기본값)
            const apiUrl = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8081';

            const response = await fetch(`${apiUrl}/api/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            });

            if (!response.ok) {
                throw new Error('로그인 실패: 아이디 또는 비밀번호를 확인하세요');
            }

            const data = await response.json();
            console.log('로그인 성공, 토큰:', data.token);

            // TODO: 토큰 저장 및 리다이렉트 로직 구현 (직접 구현하실 부분)
            // localStorage.setItem('token', data.token);
            // router.push('/');

            alert(`로그인 성공! 토큰: ${data.token}`);

        } catch (err: any) {
            setError(err.message || '로그인 중 오류가 발생했습니다.');
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

            <button type="submit" className={styles.button}>
                로그인
            </button>
        </form>
    );
}
