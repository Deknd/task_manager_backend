package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.domain.task.Task;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskRepository{

    /**
     * Ищет такск по id
     * @param id таска назначеный базой данных, при создании
     * @return возврощает полностью собраный Task
     */
    Optional<Task> findById(Long id);


    /**
     * Ищет таски по userId
     * @param userId  айди юзера
     * @return возврощает List<Task> со всеми тасками которые есть у пользователя, таски полностью заполнены
     */
    List<Task> findAllByUserId(Long userId);

    /**
     * Проверяет, принадлежит ли таск юзеру
     * @param user_id id юзера
     * @param id id таска
     * @return возврощает True, если таск принадлежит пользователю, иначе False
     */
    boolean isTaskOwner(Long user_id, Long id);

    /**
     * Обновляет данные таска: title, description, expiration_date, status, priority
     * @param task должно быть таск id
     */
    void update(Task task);

    /**
     * сохраняет таск
     * @param task userId - не может быть null, title - не может быть null, status - не может быть null, priority - не может быть null
     */
    void create(Task task);

    /**
     * Удаляет таск из памяти
     * @param id - айди таска
     */
    void delete(Long id);
}
