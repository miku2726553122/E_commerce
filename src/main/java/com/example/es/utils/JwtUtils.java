package com.example.es.utils;

import com.example.es.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

/**
 * JWT 工具类（JJWT 0.11.x）
 *
 * @author 86159
 */
@Component
public class JwtUtils {

    /** HS512 密钥，Base64 编码，配置在 application.yml 的 jwt.secret */
    @Value("${jwt.secret}")
    private String secret;

    /** 签发人 */
    @Value("${jwt.issuer}")
    private String issuer;

    /** token 过期时间，单位：分钟 */
    @Value("${jwt.tokenExpireTime}")
    private Long tokenExpireTime;

    /**
     * 生成 HMAC-SHA 密钥；secret 是 Base64 字符串，先解码再交给 Keys.hmacShaKeyFor
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException e) {
            // 兜底：如果不是合法 Base64，则按原始 UTF-8 字节使用
            keyBytes = secret.getBytes(StandardCharsets.UTF_8);
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 根据 Users 实体生成 token
     * subject = username，附加 userId / role claim
     */
    public String generateToken(Users user) {
        Date now = new Date();
        Date expireAt = new Date(now.getTime() + tokenExpireTime * 60 * 1000);

        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(expireAt)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 解析 token，返回 Claims；签名错误或过期会抛 JwtException
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 校验 token 是否合法
     */
    public boolean validateToken(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
