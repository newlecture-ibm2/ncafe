import styles from "./page.module.css";
import Link from "next/link";

export default function Home() {
  return (
    <div className={styles.page}>
      {/* Hero Section */}
      <section className={styles.hero}>
        <div className={styles.heroOverlay}></div>
        <div className={styles.heroContent}>
          <h1 className={styles.heroTitle}>
            따뜻한 커피 한 잔과 함께하는
            <br />
            <span className={styles.highlight}>당신만의 시간</span>
          </h1>
          <p className={styles.heroSubtitle}>
            공부하고, 소통하고, 창작하는 특별한 공간
            <br />
            NCafe에서 당신의 이야기를 시작하세요
          </p>
          <div className={styles.heroCta}>
            <Link href="/menu" className={styles.ctaPrimary}>
              메뉴 둘러보기
            </Link>
            <Link href="/community" className={styles.ctaSecondary}>
              커뮤니티 참여하기
            </Link>
          </div>
        </div>
        <div className={styles.scrollIndicator}>
          <span>스크롤하여 더 보기</span>
          <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
            <path d="M6 8L10 12L14 8" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
        </div>
      </section>

      {/* Features Section */}
      <section className={styles.features}>
        <div className={styles.container}>
          <h2 className={styles.sectionTitle}>NCafe가 특별한 이유</h2>
          <div className={styles.featureGrid}>
            <div className={styles.featureCard}>
              <div className={styles.featureIcon}>
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
                  <path d="M12 6.253V12L16 14M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                </svg>
              </div>
              <h3 className={styles.featureTitle}>집중할 수 있는 공간</h3>
              <p className={styles.featureDescription}>
                조용하고 편안한 분위기에서 학습과 업무에 몰입할 수 있습니다.
                넉넉한 좌석과 콘센트, 그리고 빠른 와이파이를 제공합니다.
              </p>
            </div>

            <div className={styles.featureCard}>
              <div className={styles.featureIcon}>
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
                  <path d="M17 21V19C17 17.9391 16.5786 16.9217 15.8284 16.1716C15.0783 15.4214 14.0609 15 13 15H5C3.93913 15 2.92172 15.4214 2.17157 16.1716C1.42143 16.9217 1 17.9391 1 19V21M23 21V19C22.9993 18.1137 22.7044 17.2528 22.1614 16.5523C21.6184 15.8519 20.8581 15.3516 20 15.13M16 3.13C16.8604 3.3503 17.623 3.8507 18.1676 4.55231C18.7122 5.25392 19.0078 6.11683 19.0078 7.005C19.0078 7.89317 18.7122 8.75608 18.1676 9.45769C17.623 10.1593 16.8604 10.6597 16 10.88M13 7C13 9.20914 11.2091 11 9 11C6.79086 11 5 9.20914 5 7C5 4.79086 6.79086 3 9 3C11.2091 3 13 4.79086 13 7Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                </svg>
              </div>
              <h3 className={styles.featureTitle}>따뜻한 커뮤니티</h3>
              <p className={styles.featureDescription}>
                비슷한 관심사를 가진 사람들과 소통하고 교류할 수 있습니다.
                스터디 모임부터 네트워킹까지, 함께 성장하는 즐거움을 느껴보세요.
              </p>
            </div>

            <div className={styles.featureCard}>
              <div className={styles.featureIcon}>
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none">
                  <path d="M9 11L12 14L22 4M21 12V19C21 19.5304 20.7893 20.0391 20.4142 20.4142C20.0391 20.7893 19.5304 21 19 21H5C4.46957 21 3.96086 20.7893 3.58579 20.4142C3.21071 20.0391 3 19.5304 3 19V5C3 4.46957 3.21071 3.96086 3.58579 3.58579C3.96086 3.21071 4.46957 3 5 3H16" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
                </svg>
              </div>
              <h3 className={styles.featureTitle}>프로젝트 실현</h3>
              <p className={styles.featureDescription}>
                아이디어를 현실로 만들 수 있는 협업 공간입니다.
                팀 프로젝트 공간 예약부터 멘토링까지, 당신의 꿈을 응원합니다.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Coffee & Study Section */}
      <section className={styles.coffeeStudy}>
        <div className={styles.container}>
          <div className={styles.splitContent}>
            <div className={styles.contentLeft}>
              <div className={styles.coffeeIcon}>☕</div>
              <h2 className={styles.contentTitle}>프리미엄 커피와<br />완벽한 공간의 조화</h2>
              <p className={styles.contentText}>
                바리스타가 정성스럽게 내린 커피 한 잔과 함께
                생산적인 하루를 시작하세요. 신선한 원두와 다양한 메뉴로
                당신의 취향을 만족시켜 드립니다.
              </p>
              <Link href="/menu" className={styles.contentLink}>
                메뉴 보기 →
              </Link>
            </div>
            <div className={styles.contentRight}>
              <div className={styles.statsGrid}>
                <div className={styles.statCard}>
                  <div className={styles.statNumber}>200+</div>
                  <div className={styles.statLabel}>월간 스터디 참여자</div>
                </div>
                <div className={styles.statCard}>
                  <div className={styles.statNumber}>50+</div>
                  <div className={styles.statLabel}>진행중인 프로젝트</div>
                </div>
                <div className={styles.statCard}>
                  <div className={styles.statNumber}>98%</div>
                  <div className={styles.statLabel}>고객 만족도</div>
                </div>
                <div className={styles.statCard}>
                  <div className={styles.statNumber}>24/7</div>
                  <div className={styles.statLabel}>운영 시간</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className={styles.ctaSection}>
        <div className={styles.container}>
          <h2 className={styles.ctaTitle}>지금 바로 시작하세요</h2>
          <p className={styles.ctaText}>
            NCafe에서 새로운 경험을 만들어보세요
          </p>
          <div className={styles.ctaButtons}>
            <Link href="/signup" className={styles.ctaPrimary}>
              회원가입하기
            </Link>
            <Link href="/about" className={styles.ctaSecondary}>
              더 알아보기
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
}
