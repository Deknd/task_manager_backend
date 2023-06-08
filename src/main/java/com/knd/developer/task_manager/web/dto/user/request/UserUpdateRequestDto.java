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
 * Класс для получения данных от клиента, для обновления текущих данных пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequestDto {
    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;
   // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null", groups = {OnCreate.class, OnUpdate.class})
    private String oldPassword;

    @Length(max = 255, message = "Name length must be smaller than 255 symbols.", groups = {OnCreate.class,OnUpdate.class})
    private String newName;
    @Length(max = 255, message = "Username length must be smaller than 255 symbols.", groups = {OnCreate.class,OnUpdate.class})
   // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String newUsername;
   // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String newPassword;
}
