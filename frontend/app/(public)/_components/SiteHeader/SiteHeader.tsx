'use client';

import Link from 'next/link';
import { usePathname, useRouter } from 'next/navigation';
import { useAuthStore } from '@/store/authStore';
import styles from './SiteHeader.module.css';

export default function SiteHeader() {
    const pathname = usePathname();
    const router = useRouter();
    const user = useAuthStore((state) => state.user);
    const loading = useAuthStore((state) => state.loading);
    const logout = useAuthStore((state) => state.logout);

    const handleLogout = async () => {
        await logout();
        router.push('/');
        router.refresh();
    };

    return (
        <header className={styles.header}>
            <div className={styles.inner}>
                <Link href="/" className={styles.logo}>
                    NCafe
                </Link>
                <nav className={styles.nav}>
                    <Link
                        href="/"
                        className={`${styles.navLink} ${pathname === '/' ? styles.navLinkActive : ''}`}
                    >
                        홈
                    </Link>
                    <Link
                        href="/menus"
                        className={`${styles.navLink} ${pathname.startsWith('/menus') ? styles.navLinkActive : ''}`}
                    >
                        메뉴
                    </Link>
                </nav>

                {/* 인증 영역: 로딩 중엔 빈 칸(깜빡임 방지) */}
                {!loading && (
                    user ? (
                        <div className={styles.authArea}>
                            <span className={styles.username}>{user.username}님</span>
                            <button
                                onClick={handleLogout}
                                className={styles.logoutBtn}
                            >
                                로그아웃
                            </button>
                        </div>
                    ) : (
                        <Link
                            href="/login"
                            className={`${styles.loginBtn} ${pathname === '/login' ? styles.loginBtnActive : ''}`}
                        >
                            로그인
                        </Link>
                    )
                )}
            </div>
        </header>
    );
}
