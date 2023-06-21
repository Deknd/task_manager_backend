package com.knd.developer.task_manager.web.controller;

import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.service.TaskService;
import com.knd.developer.task_manager.service.UserService;
import com.knd.developer.task_manager.service.props.TaskProperties;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import com.knd.developer.task_manager.web.dto.user.request.UserDeleteRequestDto;
import com.knd.developer.task_manager.web.dto.user.request.UserUpdateRequestDto;
import com.knd.developer.task_manager.web.dto.user.response.UserResponseDto;
import com.knd.developer.task_manager.web.dto.validation.OnCreate;
import com.knd.developer.task_manager.web.dto.validation.OnUpdate;
import com.knd.developer.task_manager.web.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserService userService;
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final TaskProperties taskProperties;

    /**
     * Обновляет пользовательские данные, такие как: имя, пароль, эмейл.
     * Если не будет id или oldPassword, вернет response со статусом BadRequest и сообщением: "Validation failed."
     * Если в полях( name, username) используются запрещеные символы( [<>&'] ), вернет response со статусом BadRequest и сообщением: "Validation failed."
     * Если пытаются обновить данные у другого пользователя или с не правильным паролем вырнет response со статусом Forbidden и сообщением:  "Access, denied"
     * @param dto - UserUpdateRequestDto обязательно должно быть заполнено: id, oldPassword
     * @return - UserResponseDto который сейчас сохранен в базе
     */
    @PutMapping("/update")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#dto.id)")
    public UserResponseDto update(@Validated(OnUpdate.class) @RequestBody UserUpdateRequestDto dto
    ) {
        return userService.update(dto);
    }

    //    @GetMapping("/{id}")
//    @Operation(summary = "Get User by Id")
//    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
//    public UserResponseDto getById(@PathVariable Long id){
//        return userService.getUser(id);
//    }

    /**
     * Проводит валидацию данных, если все хорошо, сохраняет таск в БД.
     * Если попробовать добавить таск другому пользователю, то выдаст ошибку - "Access, denied"; возвращает статус: isForbidden
     * Если title = null, то выдается ошибка - "Bad Request"; возвращает статус: isBadRequest
     * Если в title или description есть запрещенные символы, то выдаст ошибку - "Bad Request"; возвращает статус: isBadRequest
     * Если status = null, то статус поменяется на "Status.TO DO"
     * Если priority = null, то приоритет поменяется на "PriorityTask.STANDARD"
     * Если время дедлайна провалено, то статус поменяется на "Status.FAILED"
     * @param id - айди пользователя, берется из url
     * @param dto - TaskDto приходящий от клиента, title не должен быть null
     * @return - TaskDto с айди таска
     */
    @PostMapping("/{id}/tasks")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public TaskDto createTask(@PathVariable Long id,
                              @Validated(OnCreate.class) @RequestBody TaskDto dto) {
        Task createdTask = taskService.create(dto, id);
        return taskMapper.toDto(createdTask);
    }

    /**
     * Достает из БД все таски пользователя, проверяет время дедлайна, если время провалена меняет статус таски
     * Если пытаться получить таски другого пользователя, то вернется ответ со статусом IsForbidden
     * Если в URL не добавить id пользователя, то вернется ответ со статусом IsBadRequest
     * @param id - пользователя, берется из URL
     * @return - возвращает List<TaskDto> со всеми тасками пользователя TaskDto
     */
    @GetMapping("/{id}/tasks")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public List<TaskDto> getTasks(@PathVariable Long id) {
        List<Task> tasks = taskService.getAllTasksByUserId(id);
        return taskMapper.toDto(tasks);
    }

    /**
     * Удаляет из БД токен пользователя
     * Если попытаться сделать логаут другому пользователю, то вернется статус IsForbidden
     * Если не добавлять в URL айди пользователя, то вернется статус IsNotFound
     * @param id - айди пользователя, не может быть null
     */
    @DeleteMapping("/logout/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public void logout(@PathVariable Long id) {
        userService.logout(id);

    }

    /**
     * Удаляет пользователя из БД совсеми данными
     * Если попробовать удалить другого пользователя, то выдаст статус isForbidden
     * Если попробовать удалить пользователя и не добавить в урл айди, то выдаст статус isNotFound
     * @param id - айди пользователя
     */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@customSecurityExpression.canAccessUser(#id)")
    public void deleteUser(@PathVariable Long id) {
        userService.delete(id);
    }


}
