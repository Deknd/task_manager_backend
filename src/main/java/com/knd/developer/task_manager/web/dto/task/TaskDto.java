package com.knd.developer.task_manager.web.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.knd.developer.task_manager.domain.task.PriorityTask;
import com.knd.developer.task_manager.domain.task.Status;
import com.knd.developer.task_manager.web.dto.validation.OnCreate;
import com.knd.developer.task_manager.web.dto.validation.OnUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Класс для получения от пользователя нового таска
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {

    private Long id;
    @NotNull(message = "Title must be not null", groups = {OnCreate.class, OnUpdate.class})
    private String title;
    private String description;
    private Status status;
    private PriorityTask priority;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private String expirationDate;
}
