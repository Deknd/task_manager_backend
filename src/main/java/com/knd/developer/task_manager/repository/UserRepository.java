package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.domain.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
@Mapper
public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    void update(User user);
    void create(User user);
    void insertUserRole(@Param("user_id") Long userId,@Param("role") String role);
    boolean isTaskOwner(@Param("user_id") Long userId,@Param("task_id") Long taskId);
    void delete(Long id);
}
