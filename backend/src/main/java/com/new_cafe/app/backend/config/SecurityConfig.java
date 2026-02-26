package com.new_cafe.app.backend.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // @Controller , @Service , @Repository , @Configuration
// @EnableWebSecurity 
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/cookie/create").authenticated()                
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // 403 오류, DB에서는 ROLE_ADMIN으로 저장됨 
                // .requestMatchers("/api/admin/**/create").hasAuthority("MENU_CREATE") // 403 오류, DB에서는 MENU_CREATE로 저장됨 
                .requestMatchers("/cookie/session/create").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(Customizer.withDefaults());

        return http.build();
    }

    // @Bean
    // public UserDetailsService userDetailsService() {
    //     InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
    //     manager.createUser(User.withUsername("user").password(passwordEncoder().encode("1234")).roles("USER").build());
    //     return manager;
    // }

    @Bean    
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);

        // members 테이블에서 사용자 조회
        // 결과 컬럼 구조 (순서 필수):
        // +----------+--------------------------------------------------------------+---------+
        // | username | password                                                     | enabled |
        // +----------+--------------------------------------------------------------+---------+
        // | admin    | $2a$10$xK7Gq2...                                             | true    |
        // +----------+--------------------------------------------------------------+---------+
        manager.setUsersByUsernameQuery(
            "select nickname username, password, true as enabled from users where nickname = ?");

        // members 테이블에서 권한 조회
        // 결과 컬럼 구조 (순서 필수):
        // +----------+------------+
        // | username | authority  |
        // +----------+------------+
        // | admin    | ROLE_ADMIN |
        // +----------+------------+
        manager.setAuthoritiesByUsernameQuery(
            "select nickname username, role authority from users where nickname = ?");

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
