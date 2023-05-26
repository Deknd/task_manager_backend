package com.knd.developer.task_manager.web.dto.auth;

import lombok.Data;

@Data
public class JwtResponse {
    private Long id;
    private String name;
    private String accessToken;
    private String refreshToken;
    private String expiration;
}
