package com.new_cafe.app.backend.auth.domain;

public class AuthUser {
    private final Long id;
    private final String username;
    private final String password; // 실제로는 암호화된 비밀번호

    public AuthUser(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getter only (불변 객체 권장)
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
