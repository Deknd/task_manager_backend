package com.knd.developer.task_manager.service;

import com.knd.developer.task_manager.web.dto.auth.JwtRequest;
import com.knd.developer.task_manager.web.dto.auth.ResponseAuthUser;
import com.knd.developer.task_manager.web.dto.auth.RefreshRequest;

public interface AuthService {

    ResponseAuthUser login(JwtRequest loginRequest);

    ResponseAuthUser refresh(RefreshRequest refreshToken);

    void logout(String refreshToken);
}
