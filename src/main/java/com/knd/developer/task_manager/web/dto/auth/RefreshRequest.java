package com.knd.developer.task_manager.web.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshRequest {
    @NotNull(message = "Refresh token must be not null.")
    private String refreshToken;
}
