package com.knd.developer.task_manager.domain.tokens;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.util.Base64;

/**
 * Класс для хранения Рефреш токена в Редисе
 */
@Data
@RedisHash("RefreshToken")
public class Tokens implements Serializable {
    @NotNull
    private String id;
    @NotNull
    private String refreshToken;
    private String accessToken;
    @NotNull
    private String secretKey;


    /**
     * Превращает секретный ключ в строку, для хранения в редисе
     * @param secretKey - SecretKey это ключ, который необходим для расшифровки рефреш токена
     */
    public void setSecretKey(SecretKey secretKey) {
        byte[] keyBytes = secretKey.getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        this.secretKey = base64Key;
    }

    /**
     * Превращает из строки в секретный ключ
     * @return - SecretKey это ключ, который необходим для расшифровки рефреш токена
     */
    public SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey result = new SecretKeySpec(keyBytes, "HmacSHA256");
        return result;
    }
}
