package com.new_cafe.app.backend.auth.application.service;

import com.new_cafe.app.backend.auth.application.port.in.LoginUseCase;
import com.new_cafe.app.backend.auth.application.port.out.LoadUserPort;
import com.new_cafe.app.backend.auth.domain.AuthUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements LoginUseCase {

    private final LoadUserPort loadUserPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(LoadUserPort loadUserPort, 
                       PasswordEncoder passwordEncoder, 
                       JwtTokenProvider jwtTokenProvider) {
        this.loadUserPort = loadUserPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String login(LoginCommand command) {
        // 1. 사용자 조회
        AuthUser user = loadUserPort.loadUserByUsername(command.username())
            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 토큰 생성 및 반환
        return jwtTokenProvider.createTokenFromUser(user.getUsername(), user.getRole());
    }
}
