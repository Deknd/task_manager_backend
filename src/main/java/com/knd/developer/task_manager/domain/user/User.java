package com.knd.developer.task_manager.domain.user;

import com.knd.developer.task_manager.domain.task.Task;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private String passwordConfirmation;
    private Set<Role> roles;
    private List<Task> tasks;
}
