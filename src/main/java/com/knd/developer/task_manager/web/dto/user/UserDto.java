package com.knd.developer.task_manager.web.dto.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.knd.developer.task_manager.web.dto.validation.OnCreate;
import com.knd.developer.task_manager.web.dto.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
@Schema(description = "User DTO")
public class UserDto {
    @Schema(description = "User id", example = "1")
    @NotNull(message = "Id must be not null", groups = OnUpdate.class)
    private Long id;

    @Schema(description = "User name", example = "John Doe")
    @NotNull(message = "Name must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Name length must be smaller than 255 symbols.", groups = {OnCreate.class,OnUpdate.class})
    private String name;

    @Schema(description = "User email", example = "johndoe@mail.com")
    @NotNull(message = "Username must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Username length must be smaller than 255 symbols.", groups = {OnCreate.class,OnUpdate.class})
    private String username;

    @Schema(description = "User password", example = "12345")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password must be not null", groups = {OnCreate.class, OnUpdate.class})
    private String password;

    @Schema(description = "User password confirmation", example = "12345")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotNull(message = "Password confirmation must be not null", groups = OnCreate.class)
    private String passwordConfirmation;

}
