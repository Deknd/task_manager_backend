package com.knd.developer.task_manager.web.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("Refresh")
public class RefreshRequest {
    @NotNull(message = "Refresh token must be not null.")
    private String refreshToken;
}
