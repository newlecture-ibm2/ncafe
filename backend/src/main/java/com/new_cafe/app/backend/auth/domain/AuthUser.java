package com.new_cafe.app.backend.auth.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String role = "ROLE_USER"; // ROLE_USER, ROLE_ADMIN

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // JPA 기본 생성자
    public AuthUser() {}

    // 생성자
    public AuthUser(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    // 전체 생성자 (빌더 패턴 대신)
    public AuthUser(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = true;
        this.createdAt = LocalDateTime.now();
    }

    // Getter
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public boolean isEnabled() { return enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setter (JPA 필요 시)
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // 비즈니스 메서드
    public boolean isAdmin() {
        return "ROLE_ADMIN".equals(role);
    }

    public boolean hasRole(String roleName) {
        return this.role.equals(roleName);
    }
}
