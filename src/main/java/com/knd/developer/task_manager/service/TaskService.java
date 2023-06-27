package com.knd.developer.task_manager.service;

import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import com.knd.developer.task_manager.web.dto.task.TaskUpdateDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface TaskService {
    /**
     * Возврощает Таск по ID
     * @param id - id таска, выданное при сохранение его в БД
     * @return - возврощает полный таск
     */
    Task getById(Long id);
    /**
     * Возвращает все такски принадлежащие пользователю
     * @param id - id User для которого нужны таски
     * @return возврощат List<Task>, если у пользователя нет такс, возврощает пустой список
     */
    List<Task> getAllTasksByUserId(Long id);
    /**
     * Обновляет существующий таск
     *
     * @param task - таск для обновления
     * @return
     */
    Task update(TaskDto task);
    /**
     * Сохраняет полученый таск от клиента в репозитории
     * @param task - полученный от клиента таск
     * @param  id - id юзера, за которым нужно закрепить таск
     * @return - возврощает таск с id(таска) который был получен при сохранении
     */
    Task create(TaskDto task,@NotNull Long id);
    /**
     * Делает запрос в TaskRepository на удаление данного таска
     * @param id - айди таска
     */
    void delete(Long id);
    /**
     * Делает запрос в taskRepository - принадлежит ли данный таск, данному юзеру
     * @param user_id - айди юзера
     * @param task_id - айди таска
     * @return - возврощает true, если принадлежит пользователю, иначе false
     */
    boolean isTaskOwner(Long user_id, Long task_id);
}
