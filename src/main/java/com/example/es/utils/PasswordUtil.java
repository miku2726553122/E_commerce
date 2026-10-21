package com.example.es.utils;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 密码加密工具类（BCrypt）
 */
public class PasswordUtil {

    /**
     * 加密明文密码
     *
     * @param rawPassword 明文密码
     * @return BCrypt 加密后的密文
     */
    public static String encode(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
    }

    /**
     * 验证明文密码是否与密文匹配
     *
     * @param rawPassword     明文密码
     * @param encodedPassword 数据库中的密文
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return BCrypt.checkpw(rawPassword, encodedPassword);
    }

    /**
     * 校验密码强度
     * 规则：
     * 1. 长度 6-20 位
     * 2. 至少包含大写字母、小写字母、数字中的两种
     *
     * @param password 明文密码
     * @return true=强度合格，false=强度不足
     */
    public static boolean validateStrength(String password) {
        if (password == null || password.length() < 6 || password.length() > 20) {
            return false;
        }

        int complexity = 0;
        if (password.matches(".*[a-z].*")) {
            complexity++;
        }
        if (password.matches(".*[A-Z].*")) {
            complexity++;
        }
        if (password.matches(".*\\d.*")) {
            complexity++;
        }

        return complexity >= 2;
    }
}
