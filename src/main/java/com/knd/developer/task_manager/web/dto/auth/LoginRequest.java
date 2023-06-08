package com.knd.developer.task_manager.web.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс для ацетификации и авторизации пользователя, приходит от клиента
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @NotNull(message = "Username must be not null.")
    private String username;
    @NotNull(message = "Password must be not null.")
    private String password;

}
