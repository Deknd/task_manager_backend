package com.knd.developer.task_manager.web.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.knd.developer.task_manager.web.dto.validation.OnCreate;
import com.knd.developer.task_manager.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс, для получения данных от клиента, для удаления пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDeleteRequestDto {
    @NotNull(message = "Password must be not null", groups = {OnCreate.class, OnUpdate.class})
   private String password;
}
