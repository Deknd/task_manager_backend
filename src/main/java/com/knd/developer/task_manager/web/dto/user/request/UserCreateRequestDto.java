package com.knd.developer.task_manager.web.dto.user.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.knd.developer.task_manager.web.dto.validation.OnCreate;
import com.knd.developer.task_manager.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Класс для получение данных от клиента, для создания нового пользователя
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequestDto {
    @NotNull(message = "Name must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Name length must be smaller than 255 symbols.", groups = {OnCreate.class,OnUpdate.class})
    private String name;

    @NotNull(message = "Username must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Username length must be smaller than 255 symbols.", groups = {OnCreate.class,OnUpdate.class})
   // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String username;
  // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
   @NotNull(message = "Password must be not null", groups = {OnCreate.class, OnUpdate.class})
    private String password;
}
