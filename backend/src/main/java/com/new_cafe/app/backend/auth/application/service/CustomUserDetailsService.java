package com.new_cafe.app.backend.auth.application.service;

import com.new_cafe.app.backend.auth.adapter.out.persistence.UserRepository;
import com.new_cafe.app.backend.auth.domain.AuthUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security UserDetailsService 구현
 * JPA를 사용하여 데이터베이스에서 사용자 정보 조회
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 사용자명으로 사용자 정보 로드
     * Spring Security가 인증 시 자동으로 호출
     *
     * @param username 사용자명 (nickname)
     * @return UserDetails 사용자 정보
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. DB에서 사용자 조회
        AuthUser authUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                "User not found with username: " + username
            ));

        // 2. Spring Security UserDetails로 변환
        return User.builder()
            .username(authUser.getUsername())
            .password(authUser.getPassword()) // 이미 BCrypt로 암호화된 비밀번호
            .roles(extractRoleName(authUser.getRole())) // "ROLE_USER" → "USER"
            .disabled(!authUser.isEnabled())
            .accountExpired(false)
            .accountLocked(false)
            .credentialsExpired(false)
            .build();
    }

    /**
     * "ROLE_" 접두사 제거
     * Spring Security는 자동으로 "ROLE_" 추가하므로 제거 필요
     *
     * @param role "ROLE_USER" 또는 "ROLE_ADMIN"
     * @return "USER" 또는 "ADMIN"
     */
    private String extractRoleName(String role) {
        if (role != null && role.startsWith("ROLE_")) {
            return role.substring(5); // "ROLE_".length() = 5
        }
        return role;
    }
}
