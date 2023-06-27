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
     * Аутентифицирует пользователя по полученным данным, собирает ответ с полным UserAndTokenResponseDto
     * Если данные не верны, выкидывает исключение BadCredentialsException
     * @param loginRequest - JwtRequest содержит username(Not null) и  password(Not null)
     * @return - UserAndTokenResponseDto полностью заполненный пользователь с accessToken и refreshToken
     */
    @Override
    public UserAndTokenResponseDto login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));//отравляет на JwtUserDetailsService
        } catch (InternalAuthenticationServiceException e) {
            throw new BadCredentialsException(e.getMessage());
        }
        Instant validity = Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.MINUTES);
        User user = userService.getByUsername(loginRequest.getUsername()); //А если нет такого юзера? то выдается ошибка выше в методе
        return UserAndTokenResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .tasks(taskMapper.toDto(user.getTasks()))
                .expiration(validity.toString())
                .refreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()))
                .accessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles(), validity))
                .build();
    }

    /**
     * Обновляет по refreshToken -> tokens пользователя
     * @param refreshToken - RefreshRequest должен быть полностью заполненный
     * @return - UserAndTokenResponseDto полностью заполненный пользователь с tokens
     */
    @Override
    public UserAndTokenResponseDto refresh(RefreshRequest refreshToken) {
        Instant validity = Instant.now().atZone(ZoneId.of("UTC")).toInstant();
        Instant result = validity.plus(jwtProperties.getAccess(), ChronoUnit.MINUTES);
        return jwtTokenProvider.refreshUserToken(refreshToken.getRefreshToken(), result);
    }


}
