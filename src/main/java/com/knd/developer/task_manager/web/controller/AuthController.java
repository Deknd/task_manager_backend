package com.knd.developer.task_manager.web.controller;

import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.service.AuthService;
import com.knd.developer.task_manager.service.UserService;
import com.knd.developer.task_manager.web.dto.auth.JwtRequest;
import com.knd.developer.task_manager.web.dto.auth.JwtResponse;
import com.knd.developer.task_manager.web.dto.user.UserDto;
import com.knd.developer.task_manager.web.dto.validation.OnCreate;
import com.knd.developer.task_manager.web.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public JwtResponse login(@Validated @RequestBody JwtRequest loginRequest){
        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public UserDto register(@Validated(OnCreate.class) @RequestBody UserDto userDto){
        User user = userMapper.toEntity(userDto);
        User createUser= userService.create(user);
        return userMapper.toDto(createUser);
    }

    @PostMapping("/refresh")
    public JwtResponse refresh(@RequestBody String refreshToken){
        return authService.refresh(refreshToken);
    }
}
