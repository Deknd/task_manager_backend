package com.knd.developer.task_manager.repository.impl;

import com.knd.developer.task_manager.domain.task.Task;

import java.util.List;
import java.util.Optional;
import com.knd.developer.task_manager.repository.TaskRepository;
import org.springframework.stereotype.Repository;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    @Override
    public Optional<Task> findById(Long id) {
        return Optional.empty();
    }
    @Override
    public List<Task> findAllByUserId(Long userId) {
        return null;
    }
    @Override
    public void assignToUserById(Long taskId, Long userId) {}
    @Override
    public void update(Task task) {}
    @Override
    public void create(Task task) {}
    @Override
    public void delete(Long id) { }
}
