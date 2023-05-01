package com.knd.developer.task_manager.web.controller;

import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.service.TaskService;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import com.knd.developer.task_manager.web.dto.validation.OnUpdate;
import com.knd.developer.task_manager.web.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;
    @PutMapping
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto){
        Task task=taskMapper.toEntity(dto);
        Task updateTask = taskService.update(task);
        return taskMapper.toDto(updateTask);
    }
    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable Long id){
        Task task=taskService.getById(id);
        return taskMapper.toDto(task);
    }
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        taskService.delete(id);
    }
}
