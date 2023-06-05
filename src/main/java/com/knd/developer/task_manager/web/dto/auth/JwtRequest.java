package com.knd.developer.task_manager.web.dto.auth;

import com.knd.developer.task_manager.web.controller.AuthController;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest{

    @NotNull(message = "Username must be not null.")
    private String username;
    @NotNull(message = "Password must be not null.")
    private String password;

}
