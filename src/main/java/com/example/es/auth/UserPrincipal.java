package com.example.es.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * 自定义 UserDetails 实现，用于在 SecurityContext 中同时存储用户 ID 和用户名。
 * <p>
 * JwtAuthFilter 解析 token 成功后，将 userId 和 username 封装到此类中，
 * 作为 Authentication 的 principal 写入 SecurityContext。
 *
 * @author 86159
 */
@Data
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private Long userId;
    private String username;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
