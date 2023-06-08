package com.knd.developer.task_manager.web.dto.user.response;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import lombok.Data;
import java.util.List;

/**
 * Класс, для отправки клиенту обновленного пользователя
 */
@Data
public class UserAndTokenResponseDto {
    private Long id;
    private String name;
    private String accessToken;
    private String refreshToken;
    private String expiration;
    private List<TaskDto> tasks;

}
