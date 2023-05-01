package com.knd.developer.task_manager.service;

import com.knd.developer.task_manager.domain.task.Task;

import java.util.List;

public interface TaskService {
    Task getById(Long id);
    List<Task> getAllByUserId(Long id);
    Task update(Task task);
    Task create(Task task, Long id);
    void delete(Long id);
}
