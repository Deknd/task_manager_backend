package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TaskServiceImpl implements TaskService {
    @Override
    public Task getById(Long id) {
        return null;
    }
    @Override
    public List<Task> getAllByUserId(Long id) {
        return null;
    }
    @Override
    public Task update(Task task) {
        return null;
    }
    @Override
    public Task create(Task task, Long id) {
        return null;
    }
    @Override
    public void delete(Long id) {}
}
