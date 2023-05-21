package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.exception.ResourceNotFoundException;
import com.knd.developer.task_manager.domain.exception.ResourcesMappingException;
import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.repository.UserRepository;
import com.knd.developer.task_manager.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    @Caching(
            cacheable = {@Cacheable(value = "UserService::getById", key = "#id")},
            put = {@CachePut(value = "UserService::getByUsername", key = "#result.username")}
    )
    public User getById(Long id) {
        User result = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("Use not found.")));
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    @Caching(
            cacheable = {@Cacheable(value = "UserService::getByUsername", key = "#username")},
            put = {@CachePut(value = "UserService::getById", key = "#result.id")}
    )
    public User getByUsername(String username) {
        User result = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return result;
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::getById", key = "#user.id"),
            @CachePut(value = "UserService::getByUsername", key = "#user.username")
    }
    )
    public User update(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.update(user);
        return user;
    }

    @Override
    @Transactional
    @Caching(put = {
            @CachePut(value = "UserService::getById", key = "#result.id"),
            @CachePut(value = "UserService::getByUsername", key = "#result.username")
    }
    )
    public User create(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exist");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = Set.of(Role.ROLE_USER);
        user.setRoles(roles);
        userRepository.create(user);
        String role = Role.ROLE_USER.name();
        userRepository.insertUserRole(user.getId(), role);
        User result = user;
        return result;
    }

    /*  @Override
      @Transactional(readOnly = true)
      @Cacheable(value = "UserService::isTaskOwner", key = "#userId+'.'+#taskId")
      public boolean isTaskOwner(Long userId, Long taskId) {
          return userRepository.isTaskOwner(userId, taskId);
      }*/
    @Override
    @Transactional
    @Caching(
            evict = {@CacheEvict(value = "UserService::getById", key = "#id"),
                    @CacheEvict(value = "UserService::getByUsername", key = "#username"),
            }
    )
    public void delete(Long id, String username, List<Integer> id_tasks) {

        userRepository.delete(id);
    }
}
