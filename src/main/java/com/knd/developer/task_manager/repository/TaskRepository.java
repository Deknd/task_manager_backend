package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.domain.task.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskRepository{

    /**
     * Ищет task по id
     * @param id task(Long)
     * @return возвращает полностью собранный Task
     */
    Optional<Task> findById(Long id);


    /**
     * Ищет таски по userId
     * @param userId  id пользователя(Long)
     * @return возвращает List<Task> со всеми тасками которые есть у пользователя, таски полностью заполнены
     */
    List<Task> findAllByUserId(Long userId);

    /**
     * Проверяет, принадлежит ли task юзеру
     * @param user_id id пользователя(Long)
     * @param id id task(Long)
     * @return возвращает True, если task принадлежит пользователю, иначе False
     */
    boolean isTaskOwner(Long user_id, Long id);

    /**
     * Обновляет данные таска: title, description, expiration_date, status, priority
     * @param task (Task) должно быть task id
     */
    void update(Task task);

    /**
     * сохраняет task
     * @param task (Task) userId - не может быть null, title - не может быть null, status - не может быть null, priority - не может быть null
     */
    void create(Task task);

    /**
     * Удаляет task из памяти
     * @param id id task(Long)
     */
    void delete(Long id);
}
