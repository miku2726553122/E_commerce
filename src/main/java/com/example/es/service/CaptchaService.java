package com.example.es.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 图形验证码服务（Redis 存储）
 *
 * @author 86159
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CaptchaService {

    private final StringRedisTemplate redisTemplate;

    private static final String CAPTCHA_PREFIX = "captcha:";
    private static final long CAPTCHA_EXPIRE_MINUTES = 2;

    /**
     * 生成图形验证码
     *
     * @return captchaKey 和 Base64 编码的图片
     */
    public Map<String, String> generateCaptcha() {
        // 生成验证码图片: width=130, height=48, codeLength=4, lineCount=5
        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(130, 48, 4, 5);
        String code = captcha.getCode().toLowerCase();
        String key = UUID.randomUUID().toString().replace("-", "");

        // 存入 Redis，设置过期时间
        redisTemplate.opsForValue().set(
                CAPTCHA_PREFIX + key,
                code,
                CAPTCHA_EXPIRE_MINUTES,
                TimeUnit.MINUTES
        );

        log.info("[验证码] 生成成功, key={}", key);

        return Map.of(
                "captchaKey", key,
                "captchaImage", captcha.getImageBase64Data()
        );
    }

    /**
     * 校验验证码（一次性使用，校验后立即删除）
     *
     * @param captchaKey 验证码 key
     * @param code       用户输入的验证码
     * @return 是否校验通过
     */
    public boolean verifyCaptcha(String captchaKey, String code) {
        if (captchaKey == null || code == null) {
            return false;
        }
        String redisKey = CAPTCHA_PREFIX + captchaKey;
        try {
            String stored = redisTemplate.opsForValue().get(redisKey);
            if (stored == null) {
                log.info("[验证码] key={} 不存在或已过期", captchaKey);
                return false;
            }
            return stored.equalsIgnoreCase(code);
        } finally {
            // 无论校验成功失败，都删除（防止重复使用）
            redisTemplate.delete(redisKey);
        }
    }
}
