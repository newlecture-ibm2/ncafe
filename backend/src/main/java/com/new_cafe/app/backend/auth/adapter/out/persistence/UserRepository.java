package com.new_cafe.app.backend.auth.adapter.out.persistence;

import com.new_cafe.app.backend.auth.domain.AuthUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AuthUser, Long> {

    /**
     * 사용자명(nickname)으로 사용자 조회
     * @param username 사용자명
     * @return 사용자 정보 Optional
     */
    Optional<AuthUser> findByUsername(String username);

    /**
     * 사용자명 존재 여부 확인 (회원가입 시 중복 체크)
     * @param username 사용자명
     * @return 존재 여부
     */
    boolean existsByUsername(String username);
}
