package com.knd.developer.task_manager.domain.user;

import com.knd.developer.task_manager.domain.task.Task;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Используется для хранения пользователя в базе данных
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {

    private Long id;
    private String name;
    private String username;
    private String password;

    private Set<Role> roles;
    private List<Task> tasks;
}
