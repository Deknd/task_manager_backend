package com.knd.developer.task_manager.domain.refresh;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.Serializable;
import java.util.Base64;

@Data
@RedisHash("RefreshToken")

public class RefreshToken implements Serializable {
    private String id;
    private String refreshToken;
    private String secretKey;


    public void setSecretKey(SecretKey secretKey) {
        byte[] keyBytes = secretKey.getEncoded();
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        this.secretKey = base64Key;
    }

    public SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey result = new SecretKeySpec(keyBytes, "HmacSHA256");
        return result;
    }
}
