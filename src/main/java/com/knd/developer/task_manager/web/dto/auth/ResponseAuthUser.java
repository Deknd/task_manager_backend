package com.knd.developer.task_manager.web.dto.auth;

import com.knd.developer.task_manager.domain.task.Task;
import lombok.Data;

import java.util.List;

@Data
public class ResponseAuthUser {
    private Long id;
    private String name;
    private String accessToken;
    private String refreshToken;
    private String expiration;
    private List<Task> tasks;

}
