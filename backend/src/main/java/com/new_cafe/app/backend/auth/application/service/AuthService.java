package com.new_cafe.app.backend.auth.application.service;

import com.new_cafe.app.backend.auth.application.port.in.LoginUseCase;
import com.new_cafe.app.backend.auth.application.port.out.LoadUserPort;
import com.new_cafe.app.backend.auth.domain.AuthUser;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService implements LoginUseCase {

    private final LoadUserPort loadUserPort;

    public AuthService(LoadUserPort loadUserPort) {
        this.loadUserPort = loadUserPort;
    }

    @Override
    public String login(LoginCommand command) {
        // 1. 사용자 조회 (Port 호출)
        Optional<AuthUser> userOptional = loadUserPort.loadUserByUsername(command.username());

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        AuthUser user = userOptional.get();

        // 2. 비밀번호 검증 (사용자가 직접 구현할 부분)
        // TODO: 여기서 command.password()와 user.getPassword()를 비교하고 검증 로직 구현
        // 예: if (!passwordEncoder.matches(command.password(), user.getPassword())) ...

        // 3. 토큰 생성 및 반환 (사용자가 직접 구현할 부분)
        // TODO: JWT 토큰 생성 로직 구현
        return "token-placeholder-for-" + user.getUsername();
    }
}
