package com.example.es.config;

import com.example.es.filter.JwtAuthFilter;
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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // JWT 无状态，禁用 CSRF
                .csrf(csrf -> csrf.disable())
                // 不创建 Session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 请求授权
                .authorizeHttpRequests(auth -> auth
                        // 登录接口放行
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/user/**").permitAll()
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
                // 注册 JWT 过滤器
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
