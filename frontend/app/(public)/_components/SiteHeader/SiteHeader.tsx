'use client';

import Link from 'next/link';
import { usePathname } from 'next/navigation';
import styles from './SiteHeader.module.css';

export default function SiteHeader() {
    const pathname = usePathname();

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
            </div>
        </header>
    );
}
