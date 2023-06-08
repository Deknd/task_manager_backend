package com.knd.developer.task_manager.web.dto.user.response;

import com.knd.developer.task_manager.web.dto.task.TaskDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс для отправки клиенту основных данных о пользователе
 */
@Data

public class UserResponseDto {
    private Long id;
    private String name;
    private List<TaskDto> listTask;

}
