package com.knd.developer.task_manager.domain.task;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Task implements Serializable {
    private Long id;
    private Long user_id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime expirationDate;
    private  PriorityTask priority;
}
