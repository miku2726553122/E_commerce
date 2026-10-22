package com.example.es.config;

import com.example.es.filter.JwtAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security 7 配置：SecurityFilterChain Bean + Lambda DSL
 *
 * @author 86159
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 写入 JSON 错误响应
     */
    private void writeJsonError(HttpServletResponse response, int status, String message) throws java.io.IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        response.getWriter().write("{\"success\":false,\"message\":\"" + message + "\",\"data\":null}");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // JWT 无状态，禁用 CSRF
                .csrf(csrf -> csrf.disable())
                // 不创建 Session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 请求授权
                .authorizeHttpRequests(auth -> auth
                        // 登录/注册接口放行
                        .requestMatchers("/auth/**").permitAll()
                        // 商品浏览接口放行（无需登录）
                        .requestMatchers(
                                "/user/categories",
                                "/user/products",
                                "/user/products/**"
                        ).permitAll()
                        // 用户接口需要登录认证
                        .requestMatchers("/user/**").authenticated()
                        // 管理员接口需要 ADMIN 角色
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // 商家接口需要 MERCHANT 角色
                        .requestMatchers("/merchant/**").hasRole("MERCHANT")
                        // Swagger / Knife4j 文档放行
                        .requestMatchers(
                                "/doc.html",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/webjars/**",
                                "/favicon.ico"
                        ).permitAll()
                        // 其余接口需要认证
                        .anyRequest().authenticated()
                )
                // 异常处理：未认证返回 401 JSON，权限不足返回 403 JSON
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) ->
                                writeJsonError(response, HttpServletResponse.SC_UNAUTHORIZED, "未登录或token已过期"))
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                writeJsonError(response, HttpServletResponse.SC_FORBIDDEN, "权限不足"))
                )
                // 注册 JWT 过滤器
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
