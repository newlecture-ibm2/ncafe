package com.new_cafe.app.backend.auth.adapter.in.web;

import com.new_cafe.app.backend.auth.application.service.JwtTokenProvider;
import com.new_cafe.app.backend.auth.adapter.out.persistence.UserRepository;
import com.new_cafe.app.backend.auth.domain.AuthUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                         JwtTokenProvider jwtTokenProvider,
                         UserRepository userRepository,
                         PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** 현재 로그인한 사용자 정보 반환 (JWT 기반) */
    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() ||
            "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(401).build();
        }
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return ResponseEntity.ok(new MeResponse(authentication.getName(), roles));
    }

    /** 회원가입 처리 (테스트용) */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // 1. 중복 확인
        if (userRepository.existsByUsername(request.username())) {
            return ResponseEntity.status(400).body(new ErrorResponse("이미 존재하는 사용자입니다."));
        }

        // 2. 사용자 생성
        AuthUser user = new AuthUser();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok(new SuccessResponse("회원가입이 완료되었습니다."));
    }

    /** 로그인 처리 (JWT 토큰 발급) */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            // 1. 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.username(), request.password());

            // 2. AuthenticationManager로 인증 수행
            Authentication authentication = authenticationManager.authenticate(authToken);

            // 3. JWT 토큰 생성
            String token = jwtTokenProvider.createToken(authentication);

            // 4. 사용자 정보와 토큰 반환
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return ResponseEntity.ok(new LoginResponse(
                authentication.getName(),
                roles,
                token
            ));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(new ErrorResponse("아이디 또는 비밀번호가 올바르지 않습니다."));
        }
    }

    // DTOs
    public record RegisterRequest(String username, String password) {}
    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String username, List<String> roles, String token) {}
    public record MeResponse(String username, List<String> roles) {}
    public record SuccessResponse(String message) {}
    public record ErrorResponse(String error) {}
}
