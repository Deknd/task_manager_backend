package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.service.AuthService;
import com.knd.developer.task_manager.web.dto.auth.JwtRequest;
import com.knd.developer.task_manager.web.dto.auth.JwtResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public JwtResponse login(JwtRequest loginRequest) {
        return null;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        return null;
    }
}
