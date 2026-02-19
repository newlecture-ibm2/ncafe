package com.new_cafe.app.backend.auth.adapter.out.persistence;

import com.new_cafe.app.backend.auth.application.port.out.LoadUserPort;
import com.new_cafe.app.backend.auth.domain.AuthUser;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class AuthPersistenceAdapter implements LoadUserPort {

    // private final UserRepository userRepository; // JPA Repository 주입 필요

    @Override
    public Optional<AuthUser> loadUserByUsername(String username) {
        // TODO: 실제 DB에서 사용자 조회 로직 구현 (JPA, MyBatis 등)
        // 예: UserEntity entity = userRepository.findByUsername(username);
        //     return Optional.of(new AuthUser(entity.getId(), entity.getUsername(), entity.getPassword()));
        
        // 임시 테스트용 데이터 반환
        if ("admin".equals(username)) {
            return Optional.of(new AuthUser(1L, "admin", "password123"));
        }
        return Optional.empty();
    }
}
