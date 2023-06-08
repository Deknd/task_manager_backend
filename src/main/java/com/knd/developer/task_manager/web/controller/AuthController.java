package com.knd.developer.task_manager.web.controller;

import com.knd.developer.task_manager.service.AuthService;
import com.knd.developer.task_manager.service.UserService;
import com.knd.developer.task_manager.web.dto.auth.LoginRequest;
import com.knd.developer.task_manager.web.dto.auth.RefreshRequest;
import com.knd.developer.task_manager.web.dto.user.response.UserAndTokenResponseDto;
import com.knd.developer.task_manager.web.dto.user.request.UserCreateRequestDto;
import com.knd.developer.task_manager.web.dto.validation.OnCreate;
import com.knd.developer.task_manager.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/login")
    public UserAndTokenResponseDto login(@Validated @RequestBody LoginRequest loginRequest) {

        return authService.login(loginRequest);
    }

    @PostMapping("/register")
    public void register(@Validated(OnCreate.class) @RequestBody UserCreateRequestDto userDto) {

        userService.create(userDto);
    }

    @PostMapping("/refresh")
    public UserAndTokenResponseDto refresh(@RequestBody RefreshRequest refreshToken) {
        return authService.refresh(refreshToken);
    }

}
