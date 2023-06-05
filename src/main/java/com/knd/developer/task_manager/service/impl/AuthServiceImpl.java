package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.service.AuthService;
import com.knd.developer.task_manager.service.UserService;
import com.knd.developer.task_manager.service.props.JwtProperties;
import com.knd.developer.task_manager.web.dto.auth.JwtRequest;
import com.knd.developer.task_manager.web.dto.auth.ResponseAuthUser;
import com.knd.developer.task_manager.web.dto.auth.RefreshRequest;
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


    @Override
    public ResponseAuthUser login(JwtRequest loginRequest) {
        ResponseAuthUser responseAuthUser = new ResponseAuthUser();//создаем ответ на запрос
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));//отравляет на JwtUserDetailsService
        } catch (InternalAuthenticationServiceException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        Instant validity = Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.MINUTES);

        User user = userService.getByUsername(loginRequest.getUsername()); //А если нет такого юзера? то выдается ошибка выше в методе
        responseAuthUser.setId(user.getId());
        responseAuthUser.setName(user.getName());
        responseAuthUser.setTasks(user.getTasks());
        responseAuthUser.setExpiration(validity.toString());
        responseAuthUser.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles(), validity));

        responseAuthUser.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()));

        return responseAuthUser;
    }

    @Override
    public ResponseAuthUser refresh(RefreshRequest refreshToken) {
        Instant validity = Instant.now().atZone(ZoneId.of("UTC")).toInstant();
        Instant result = validity.plus(jwtProperties.getAccess(), ChronoUnit.MINUTES);
        return jwtTokenProvider.refreshUserToken(refreshToken.getRefreshToken(), result);
    }

    @Override
    public void logout(String refreshToken) {
        jwtTokenProvider.logoutUser(refreshToken);
    }
}
