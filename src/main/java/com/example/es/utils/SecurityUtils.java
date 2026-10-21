package com.example.es.utils;

import com.example.es.auth.UserPrincipal;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Spring Security 上下文工具类
 * <p>
 * JwtAuthFilter 校验 token 通过后，会把 username 作为 principal 写入 SecurityContext，
 * 这里统一收口获取当前登录用户的逻辑，避免各处直接依赖 SecurityContextHolder。
 *
 * @author 86159
 */
public class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * 获取当前登录用户名
     *
     * @return 当前登录用户名；未登录（无认证信息或匿名用户）时返回 null
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUsername();
        }
        return authentication.getName();
    }

    /**
     * 获取当前登录用户 ID
     *
     * @return 当前登录用户 ID；未登录时返回 null
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserPrincipal userPrincipal) {
            return userPrincipal.getUserId();
        }
        return null;
    }

    /**
     * 判断当前请求是否已登录
     *
     * @return 已登录返回 true
     */
    public static boolean isAuthenticated() {
        return getCurrentUsername() != null;
    }
}
