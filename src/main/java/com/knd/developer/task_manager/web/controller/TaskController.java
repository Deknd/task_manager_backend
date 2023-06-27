package com.knd.developer.task_manager.web.controller;

import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.service.TaskService;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import com.knd.developer.task_manager.web.dto.task.TaskUpdateDto;
import com.knd.developer.task_manager.web.dto.validation.OnUpdate;
import com.knd.developer.task_manager.web.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    /**
     * Обновит данные в таске, если они проходят валидацию(без запрещенных знаков, не пустые, обязательно должен быть id у task)
     * Данные можно обновлять по одному полю
     * Если попробовать обновить task не принадлежащий авторизованному пользователю, то вернется сообщение со статусом IsForbidden
     * Если переданные данные равны null, то эти поля не будут обновляться
     * Если данные приходят с запрещенными символами или слишком длинные, то вернется ответ со статусом BadRequest
     * Если попробовать передать task без id, то вернется сообщение со статусом IsForbidden
     *
     * @param dto - TaskDto(id - notNull)
     * @return - обновленный TaskDto
     */
    @PutMapping("/update")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#dto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto){
        Task updateTask = taskService.update(dto);
        return taskMapper.toDto(updateTask);
    }

    /**
     * Ищет по id(task) в БД task и возвращает его пользователю
     * Если попробовать получить чужой таск, то вернется сообщение со статусом IsForbidden
     * Если не передавать id(task), то вернется сообщение со статусом IsNotFound
     * @param id - id(task), который принадлежит пользователю
     * @return - TaskDto найденное по данному id
     */
    @GetMapping("/{id}/get")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public TaskDto getTask(@PathVariable Long id){
        Task task=taskService.getById(id);
        return taskMapper.toDto(task);
    }
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void deleteTask(@PathVariable Long id){
        taskService.delete(id);
    }
}
