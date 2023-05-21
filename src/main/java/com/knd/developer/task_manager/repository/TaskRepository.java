package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.domain.task.Task;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskRepository{
    Optional<Task> findById(Long id);
    List<Task> findAllByUserId(Long userId);
   // void assignToUserById(@Param("task_id") Long task_id,@Param("user_id") Long user_id);

    List<Integer> findAllTaskIdsByUserId(Long user_id);
    boolean isTaskOwner(Long user_id, Long id);
    void update(Task task);
    void create(Task task);
    void delete(Long id);
}
