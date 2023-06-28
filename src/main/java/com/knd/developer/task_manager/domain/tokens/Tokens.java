package com.knd.developer.task_manager.domain.tokens;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.util.Base64;

/**
 * Класс для хранения Рефреш токена в Редисе
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("RefreshToken")
public class Tokens implements Serializable {
    @NotNull
    private String id;
    @NotNull
    private String refreshToken;
    private String accessToken;
    @NotNull
    private String secretKey;

    public static TokensBuilder builder() {
        return new TokensBuilder();
    }


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
        if(this.secretKey == null) return null;
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey result = new SecretKeySpec(keyBytes, "HmacSHA256");
        return result;
    }

    public static class TokensBuilder {
        private @NotNull String id;
        private @NotNull String refreshToken;
        private String accessToken;
        private @NotNull String secretKey;

        TokensBuilder() {
        }

        public TokensBuilder id(@NotNull String id) {
            this.id = id;
            return this;
        }

        public TokensBuilder refreshToken(@NotNull String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public TokensBuilder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public TokensBuilder secretKey(@NotNull SecretKey secretKey) {
            byte[] keyBytes = secretKey.getEncoded();
            this.secretKey = Base64.getEncoder().encodeToString(keyBytes);
            return this;
        }

        public Tokens build() {
            return new Tokens(id, refreshToken, accessToken, secretKey);
        }

        public String toString() {

            return "Tokens.TokensBuilder(id=" + this.id + ", refreshToken=" + this.refreshToken + ", accessToken=" + this.accessToken + ", secretKey=" + this.secretKey + ")";
        }
    }
}
