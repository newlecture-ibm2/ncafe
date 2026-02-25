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
                .requestMatchers("/cookie/session/create").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(Customizer.withDefaults());

        return http.build();
    }

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
            "SELECT username, password, enabled FROM members WHERE username = ?");

        // members 테이블에서 권한 조회
        // 결과 컬럼 구조 (순서 필수):
        // +----------+------------+
        // | username | authority  |
        // +----------+------------+
        // | admin    | ROLE_ADMIN |
        // +----------+------------+
        manager.setAuthoritiesByUsernameQuery(
            "SELECT username, CONCAT('ROLE_', role) FROM members WHERE username = ?");

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
