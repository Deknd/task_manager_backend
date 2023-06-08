package com.knd.developer.task_manager.service;

import com.knd.developer.task_manager.web.dto.auth.LoginRequest;
import com.knd.developer.task_manager.web.dto.user.response.UserAndTokenResponseDto;
import com.knd.developer.task_manager.web.dto.auth.RefreshRequest;

public interface AuthService {

    /**
     * Выполняет аутентификацию пользователя, сверяет предоставленный пароль с сохраненным, создает токены доступа
     * @param loginRequest - JwtRequest содержит username и  password, должен быть полностью заполненным
     * @return - ResponseAuthUser полностью заполненный пользователь с токенами
     */
    UserAndTokenResponseDto login(LoginRequest loginRequest);

    /**
     * Обновляет токены доступа
     * @param refreshToken - RefreshRequest должен быть полностью заполненный
     * @return - ResponseAuthUser полностью заполненный пользователь с токенами
     */
    UserAndTokenResponseDto refresh(RefreshRequest refreshToken);


}
