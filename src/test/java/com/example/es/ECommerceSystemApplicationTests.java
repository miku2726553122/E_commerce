package com.example.es;

import com.example.es.entity.Users;
import com.example.es.mapper.UsersMapper;
import com.example.es.utils.PasswordUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ECommerceSystemApplicationTests {

    @Autowired
    private UsersMapper usersMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testSelectByIdAndPasswordMatch() {
        // 测试 selectById 是否能正确获取 password_hash
        Users user = usersMapper.selectById(6L);
        System.out.println("=== selectById 结果 ===");
        System.out.println("user: " + user);
        System.out.println("passwordHash: [" + user.getPasswordHash() + "]");
        System.out.println("passwordHash is null: " + (user.getPasswordHash() == null));
        System.out.println("passwordHash is empty: " + (user.getPasswordHash() != null && user.getPasswordHash().isEmpty()));

        // 测试 BCrypt 匹配
        String testPassword = "12345678";
        String encoded = PasswordUtil.encode(testPassword);
        System.out.println("\n=== BCrypt 测试 ===");
        System.out.println("明文密码: " + testPassword);
        System.out.println("新生成的哈希: " + encoded);
        System.out.println("数据库中的哈希: " + user.getPasswordHash());
        System.out.println("matches(新密码, 数据库哈希): " + PasswordUtil.matches(testPassword, user.getPasswordHash()));
        System.out.println("matches(新密码, 新生成哈希): " + PasswordUtil.matches(testPassword, encoded));
    }

    @Test
    void encodePassword() {
        String password = "12345678";
        String encoded = PasswordUtil.encode(password);
        System.out.println("Encoded password: " + encoded);
    }

}
