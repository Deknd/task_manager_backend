package com.knd.developer.task_manager.web.dto.user.response;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Класс, для отправки клиенту обновленного пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAndTokenResponseDto {
    private Long id;
    private String name;
    private String accessToken;
    private String refreshToken;
    private String expiration;
    private List<TaskDto> tasks;

}
