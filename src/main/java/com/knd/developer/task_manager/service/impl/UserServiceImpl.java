package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public User getById(Long id) {
        return null;
    }
    @Override
    public User getByUsername(String username) {
        return null;
    }
    @Override
    public User update(User user) {
        return null;
    }
    @Override
    public User create(User user) {
        return null;
    }
    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        return false;
    }
    @Override
    public void delete(Long id) {}
}
