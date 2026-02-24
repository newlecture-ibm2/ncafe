import Link from 'next/link';
import styles from './SiteFooter.module.css';

export default function SiteFooter() {
    return (
        <footer className={styles.footer}>
            <div className={styles.inner}>
                <div className={styles.top}>
                    <div className={styles.brand}>
                        <span className={styles.logo}>NCafe</span>
                        <p className={styles.tagline}>따뜻한 커피 한 잔과 함께하는 당신만의 시간</p>
                    </div>
                    <nav className={styles.nav}>
                        <Link href="/" className={styles.navLink}>홈</Link>
                        <Link href="/menus" className={styles.navLink}>메뉴</Link>
                    </nav>
                </div>

                <div className={styles.divider} />

                <div className={styles.bottom}>
                    <div className={styles.info}>
                        <span>대표자: 뉴렉</span>
                        <span className={styles.dot} />
                        <span>사업자번호: 132-18-11111</span>
                        <span className={styles.dot} />
                        <span>연락처: 02-9999-8888</span>
                        <span className={styles.dot} />
                        <span>이메일: support@new-cafe.com</span>
                    </div>
                    <p className={styles.copyright}>&copy; {new Date().getFullYear()} NCafe. All rights reserved.</p>
                </div>
            </div>
        </footer>
    );
}
