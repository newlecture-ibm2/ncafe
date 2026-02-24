# NCafe 인증/권한 구현 플랜

> **목표**: `/admin/**` 페이지에 대해 관리자(ADMIN) 역할을 가진 사용자만 접근할 수 있도록  
> 로그인, JWT 인증, 권한 검증을 구현한다.

---

## 📋 현재 상태 분석

### 이미 구현된 것
| 항목 | 상태 | 위치 |
|------|------|------|
| 로그인 API 엔드포인트 | ✅ 스켈레톤 | `auth/adapter/in/web/AuthController.java` |
| LoginUseCase (Port In) | ✅ 존재 | `auth/application/port/in/LoginUseCase.java` |
| LoadUserPort (Port Out) | ✅ 존재 | `auth/application/port/out/LoadUserPort.java` |
| AuthService | ✅ 스켈레톤 (TODO 다수) | `auth/application/service/AuthService.java` |
| AuthPersistenceAdapter | ✅ 하드코딩 임시 데이터 | `auth/adapter/out/persistence/AuthPersistenceAdapter.java` |
| AuthUser 도메인 | ✅ 기본 (role 없음) | `auth/domain/AuthUser.java` |
| 프론트 로그인 페이지 | ✅ UI만 존재 | `frontend/app/login/` |
| 프론트 Admin 레이아웃 | ✅ 권한 체크 없음 | `frontend/app/admin/layout.tsx` |

### 구현이 필요한 것
- ❌ Spring Security 의존성 없음
- ❌ JWT 라이브러리 없음
- ❌ `users` 테이블 / Entity 없음
- ❌ 비밀번호 암호화 (BCrypt) 없음
- ❌ Role(역할) 개념 없음
- ❌ JWT 토큰 생성/검증 로직 없음
- ❌ API 요청 시 토큰 검증 필터 없음
- ❌ 프론트엔드 인증 상태 관리 없음
- ❌ 프론트엔드 Admin 페이지 접근 제어 없음

---

## 🏗️ 구현 단계

### Phase 1: 의존성 추가

**파일**: `backend/build.gradle`

```gradle
dependencies {
    // 기존 의존성...
    
    // 추가할 의존성
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
    runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'
}
```

---

### Phase 2: DB - users 테이블 및 Entity

#### 2-1. DB 스키마 (자동 생성 또는 수동)

```sql
CREATE TABLE users (
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(50)  UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,          -- BCrypt 해시
    role        VARCHAR(20)  NOT NULL DEFAULT 'USER',  -- 'ADMIN' | 'USER'
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

#### 2-2. JPA Entity 생성

**파일**: `auth/adapter/out/persistence/UserEntity.java`

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | PK, auto-increment |
| username | String | 로그인 ID, unique |
| password | String | BCrypt 암호화 비밀번호 |
| role | String (또는 Enum) | `ADMIN` / `USER` |
| createdAt | LocalDateTime | 생성 시각 |
| updatedAt | LocalDateTime | 수정 시각 |

#### 2-3. JPA Repository 생성

**파일**: `auth/adapter/out/persistence/UserJpaRepository.java`

```java
public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
```

#### 2-4. 초기 Admin 계정 시드 데이터

**파일**: `config/DataInitializer.java`에 추가

```java
private void initUsers() {
    Integer userCount = jdbc.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
    if (userCount != null && userCount > 0) return;

    // BCrypt 해시된 비밀번호 삽입
    String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
    jdbc.update(sql, "admin", BCrypt.hashpw("admin1234", BCrypt.gensalt()), "ADMIN");
}
```

---

### Phase 3: 도메인 모델 보완

#### 3-1. AuthUser에 Role 추가

**파일**: `auth/domain/AuthUser.java`

```java
public class AuthUser {
    private final Long id;
    private final String username;
    private final String password;
    private final String role;  // ← 추가

    // 생성자, getter 업데이트
}
```

---

### Phase 4: JWT 토큰 서비스

#### 4-1. JWT 설정 프로퍼티

**파일**: `application.properties`에 추가

```properties
# JWT 설정
jwt.secret=이곳에-256비트-이상의-시크릿-키
jwt.expiration=3600000  # 1시간 (밀리초)
```

#### 4-2. JWT 유틸리티 / 포트

헥사고날 아키텍처에 맞게 JWT 관련 기능을 Port로 추상화한다.

**파일**: `auth/application/port/out/JwtTokenPort.java`

```java
public interface JwtTokenPort {
    String generateToken(AuthUser user);        // 토큰 생성
    String extractUsername(String token);        // 토큰에서 username 추출
    String extractRole(String token);            // 토큰에서 role 추출
    boolean validateToken(String token);         // 토큰 유효성 검증
}
```

**파일**: `auth/adapter/out/jwt/JwtTokenAdapter.java`

- jjwt 라이브러리를 사용하여 구현
- Claims에 `username`, `role` 포함
- 만료 시간 설정

---

### Phase 5: 인증 서비스 완성

#### 5-1. AuthService 비밀번호 검증 구현

**파일**: `auth/application/service/AuthService.java`

```java
@Service
public class AuthService implements LoginUseCase {

    private final LoadUserPort loadUserPort;
    private final JwtTokenPort jwtTokenPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public String login(LoginCommand command) {
        // 1. 사용자 조회
        AuthUser user = loadUserPort.loadUserByUsername(command.username())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. JWT 토큰 생성 및 반환
        return jwtTokenPort.generateToken(user);
    }
}
```

#### 5-2. AuthPersistenceAdapter DB 연동

**파일**: `auth/adapter/out/persistence/AuthPersistenceAdapter.java`

- 하드코딩 제거
- `UserJpaRepository` 주입
- `UserEntity` → `AuthUser` 변환

---

### Phase 6: Spring Security 설정

#### 6-1. SecurityConfig

**파일**: `config/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // 공개 API
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/menus/**").permitAll()     // 일반 사용자 메뉴 조회
                .requestMatchers(HttpMethod.GET, "/**").permitAll() // 정적 리소스
                
                // 관리자 전용 API  
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // 나머지
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, 
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

#### 6-2. JWT 인증 필터

**파일**: `config/JwtAuthenticationFilter.java`

```
역할:
1. 요청 헤더에서 "Authorization: Bearer <token>" 추출
2. JwtTokenPort를 사용하여 토큰 유효성 검증
3. 유효하면 SecurityContext에 Authentication 객체 설정
4. 유효하지 않으면 pass (Spring Security가 401 반환)
```

#### 6-3. CORS 설정 업데이트

**파일**: `config/WebConfig.java` 수정

- Spring Security의 CORS 설정과 통합
- credentials: true 유지 (쿠키/인증 헤더 전달)

---

### Phase 7: 프론트엔드 - 인증 상태 관리

#### 7-1. API 클라이언트 설정

**파일**: `frontend/lib/api.ts` (신규)

```typescript
// 인증된 API 요청을 위한 헬퍼
// - localStorage에서 토큰 읽기
// - Authorization 헤더에 Bearer 토큰 추가
// - 401 응답 시 로그인 페이지로 리다이렉트
```

#### 7-2. 로그인 폼 개선

**파일**: `frontend/app/login/LoginForm.tsx` 수정

```
변경사항:
1. 로그인 성공 시 토큰을 localStorage에 저장
2. useRouter()를 사용하여 /admin으로 리다이렉트
3. alert() 제거, 실제 동작으로 교체
```

#### 7-3. 인증 컨텍스트 (선택사항)

**파일**: `frontend/contexts/AuthContext.tsx` (신규)

```
역할:
- 전역 인증 상태 관리 (로그인 여부, 사용자 정보)
- login(), logout() 함수 제공
- 토큰 만료 시 자동 로그아웃
```

---

### Phase 8: 프론트엔드 - Admin 페이지 접근 제어

#### 8-1. Next.js Middleware

**파일**: `frontend/middleware.ts` (신규)

```typescript
// /admin/** 경로 접근 시:
// 1. 쿠키 또는 헤더에서 토큰 확인
// 2. 토큰이 없으면 → /login으로 리다이렉트
// 3. 토큰이 있으면 → 요청 통과

export const config = {
    matcher: '/admin/:path*'
};
```

> **참고**: Next.js Middleware는 서버 사이드에서 실행되므로  
> localStorage에 직접 접근 불가 → **쿠키 기반 토큰 저장** 또는  
> **클라이언트에서 검증** 방식 중 선택 필요

#### 8-2. Admin 레이아웃에 인증 체크 추가

**파일**: `frontend/app/admin/layout.tsx` 수정

```
변경사항:
- 클라이언트 사이드에서 토큰 유효성 확인
- 미인증 시 /login 리다이렉트
- 로딩 상태 표시
```

---

## 📁 최종 파일 구조 (변경/추가 파일)

```
backend/
├── build.gradle                                    # [수정] 의존성 추가
├── src/main/resources/
│   └── application.properties                      # [수정] JWT 설정 추가
└── src/main/java/com/new_cafe/app/backend/
    ├── config/
    │   ├── SecurityConfig.java                     # [추가] Spring Security 설정
    │   ├── JwtAuthenticationFilter.java            # [추가] JWT 필터
    │   ├── DataInitializer.java                    # [수정] admin 계정 시드
    │   └── WebConfig.java                          # [수정] CORS 통합
    └── auth/
        ├── domain/
        │   └── AuthUser.java                       # [수정] role 필드 추가
        ├── application/
        │   ├── port/
        │   │   ├── in/
        │   │   │   └── LoginUseCase.java           # [유지]
        │   │   └── out/
        │   │       ├── LoadUserPort.java           # [유지]
        │   │       └── JwtTokenPort.java           # [추가] JWT 추상화
        │   └── service/
        │       └── AuthService.java                # [수정] 로직 완성
        └── adapter/
            ├── in/web/
            │   └── AuthController.java             # [수정] 에러 핸들링 추가
            └── out/
                ├── persistence/
                │   ├── AuthPersistenceAdapter.java  # [수정] DB 조회로 변경
                │   ├── UserEntity.java             # [추가]
                │   └── UserJpaRepository.java      # [추가]
                └── jwt/
                    └── JwtTokenAdapter.java         # [추가] JWT 구현체

frontend/
├── middleware.ts                                    # [추가] Admin 접근 제어
├── lib/
│   └── api.ts                                      # [추가] 인증 API 클라이언트
├── contexts/
│   └── AuthContext.tsx                              # [추가] 인증 상태 관리 (선택)
└── app/
    ├── login/
    │   └── LoginForm.tsx                            # [수정] 실제 로그인 동작
    └── admin/
        └── layout.tsx                               # [수정] 인증 체크 추가
```

---

## 🔄 구현 순서 (권장)

```
Phase 1 → Phase 2 → Phase 3 → Phase 4 → Phase 5 → Phase 6 → Phase 7 → Phase 8
  의존성     DB/Entity    도메인      JWT       서비스     Security   프론트인증   프론트권한
```

각 Phase가 완료될 때마다 **컴파일 및 테스트**를 수행하여 점진적으로 검증한다.

---

## ⚠️ 고려사항

### 보안
- JWT Secret Key는 환경변수로 관리 (코드에 하드코딩 금지)
- 비밀번호는 반드시 BCrypt로 해싱
- Refresh Token은 이번 구현 범위에서 제외 (추후 확장)

### 토큰 저장 방식 (프론트엔드)
| 방식 | 장점 | 단점 |
|------|------|------|
| **localStorage** | 간단한 구현 | XSS 공격에 취약 |
| **httpOnly Cookie** | XSS 안전 | CSRF 대응 필요, 서버 설정 필요 |

> **권장**: 초기 구현은 `localStorage`로 시작하고, 이후 보안 강화 시 httpOnly Cookie로 변경

### DDL 전략
- 현재 `spring.jpa.hibernate.ddl-auto=create`로 되어 있어, 서버 재시작 시 데이터가 초기화됨
- 프로덕션 전환 시 `validate` 또는 `none`으로 변경 필요

---

## 🧪 테스트 시나리오

1. **로그인 성공**: `admin` / `admin1234` → JWT 토큰 반환
2. **로그인 실패**: 잘못된 비밀번호 → 401 에러
3. **인증 없이 Admin API 접근**: `/api/admin/menus` → 401 에러
4. **유효한 토큰으로 Admin API 접근**: Authorization 헤더 포함 → 200 성공
5. **일반 사용자(USER role)로 Admin 접근**: → 403 Forbidden
6. **프론트 Admin 페이지**: 미로그인 시 `/login`으로 리다이렉트
7. **프론트 Admin 페이지**: 로그인 후 정상 접근
