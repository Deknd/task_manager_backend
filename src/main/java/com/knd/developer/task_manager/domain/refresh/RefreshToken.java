package com.knd.developer.task_manager.domain.refresh;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("RefreshToken")

public class RefreshToken implements Serializable {
    private String id;
    private String refreshToken;
}
