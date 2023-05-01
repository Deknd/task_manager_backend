package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.domain.task.Task;
import java.util.List;
import java.util.Optional;


public interface TaskRepository{
    Optional<Task> findById(Long id);
    List<Task> findAllByUserId(Long userId);
    void assignToUserById(Long taskId,Long userId);
    void update(Task task);
    void create(Task task);
    void delete(Long id);
}
