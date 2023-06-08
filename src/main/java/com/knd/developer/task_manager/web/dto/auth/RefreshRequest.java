package com.knd.developer.task_manager.web.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для получения рефреш токена от клиента
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshRequest {
    @NotNull(message = "Refresh token must be not null.")
    private String refreshToken;
}
