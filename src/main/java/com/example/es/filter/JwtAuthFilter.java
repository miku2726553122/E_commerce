package com.example.es.filter;

import com.example.es.auth.UserPrincipal;
import com.example.es.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT 认证过滤器：解析请求头 Authorization 中的 Bearer token，
 * 校验通过后写入 SecurityContext。
 *
 * @author 86159
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain chain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        log.info("==========> {} {}", method, path);

        String header = request.getHeader(HEADER);
        if (header == null || !header.startsWith(PREFIX)) {
            // 未携带 token，直接放行；后续由 Security 决定是否 401
            log.debug("[JWT] 未携带 Token, path={}", path);
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(PREFIX.length()).trim();
        try {
            Claims claims = jwtUtils.parseToken(token);
            String username = claims.getSubject();
            Long userId = claims.get("userId", Long.class);
            String role = claims.get("role", String.class);
            String authority = (role == null || role.isBlank())
                    ? "ROLE_USER"
                    : (role.startsWith("ROLE_") ? role : "ROLE_" + role);

            UserPrincipal principal = new UserPrincipal(userId, username);
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    principal, null, AuthorityUtils.createAuthorityList(authority)
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            log.info("[JWT] 认证成功: userId={}, username={}, role={}", userId, username, role);
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("[JWT] Token 解析失败: path={}, error={}", path, e.getMessage());
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }
}
