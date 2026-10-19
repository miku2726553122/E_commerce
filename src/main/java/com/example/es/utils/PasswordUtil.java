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
}
