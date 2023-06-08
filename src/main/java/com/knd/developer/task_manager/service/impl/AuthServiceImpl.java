package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.service.AuthService;
import com.knd.developer.task_manager.service.UserService;
import com.knd.developer.task_manager.service.props.JwtProperties;
import com.knd.developer.task_manager.web.dto.auth.LoginRequest;
import com.knd.developer.task_manager.web.dto.auth.RefreshRequest;
import com.knd.developer.task_manager.web.dto.user.response.UserAndTokenResponseDto;
import com.knd.developer.task_manager.web.mappers.TaskMapper;
import com.knd.developer.task_manager.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final TaskMapper taskMapper;


    /**
     * Выполняет аутентификацию пользователя, сверяет предоставленный пароль с сохраненным, создает токены доступа
     * @param loginRequest - JwtRequest содержит username и  password, должен быть полностью заполненным
     * @return - ResponseAuthUser полностью заполненный пользователь с токенами
     */
    @Override
    public UserAndTokenResponseDto login(LoginRequest loginRequest) {
        UserAndTokenResponseDto userAndTokenResponseDTO = new UserAndTokenResponseDto();//создаем ответ на запрос
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));//отравляет на JwtUserDetailsService
        } catch (InternalAuthenticationServiceException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        Instant validity = Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.MINUTES);

        User user = userService.getByUsername(loginRequest.getUsername()); //А если нет такого юзера? то выдается ошибка выше в методе
        userAndTokenResponseDTO.setId(user.getId());
        userAndTokenResponseDTO.setName(user.getName());
        userAndTokenResponseDTO.setTasks(taskMapper.toDto(user.getTasks()));
        userAndTokenResponseDTO.setExpiration(validity.toString());

        userAndTokenResponseDTO.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()));

        userAndTokenResponseDTO.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles(), validity));


        return userAndTokenResponseDTO;
    }

    /**
     * Обновляет токены доступа
     * @param refreshToken - RefreshRequest должен быть полностью заполненный
     * @return - ResponseAuthUser полностью заполненный пользователь с токенами
     */
    @Override
    public UserAndTokenResponseDto refresh(RefreshRequest refreshToken) {
        Instant validity = Instant.now().atZone(ZoneId.of("UTC")).toInstant();
        Instant result = validity.plus(jwtProperties.getAccess(), ChronoUnit.MINUTES);
        return jwtTokenProvider.refreshUserToken(refreshToken.getRefreshToken(), result);
    }


}
