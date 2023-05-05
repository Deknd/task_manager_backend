package com.knd.developer.task_manager.web.controller;

import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.service.TaskService;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import com.knd.developer.task_manager.web.dto.validation.OnUpdate;
import com.knd.developer.task_manager.web.mappers.TaskMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task Controller", description = "Task API")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    @PutMapping
    @Operation(summary = "Udate task")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#dto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto){
        Task task=taskMapper.toEntity(dto);
        Task updateTask = taskService.update(task);
        return taskMapper.toDto(updateTask);
    }
    @GetMapping("/{id}")
    @Operation(summary = "Get task by Id")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public TaskDto getById(@PathVariable Long id){
        Task task=taskService.getById(id);
        return taskMapper.toDto(task);
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete task")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void deleteById(@PathVariable Long id){
        taskService.delete(id);
    }
}
