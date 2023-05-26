package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.service.AuthService;
import com.knd.developer.task_manager.service.UserService;
import com.knd.developer.task_manager.service.props.JwtProperties;
import com.knd.developer.task_manager.web.dto.auth.JwtRequest;
import com.knd.developer.task_manager.web.dto.auth.JwtResponse;
import com.knd.developer.task_manager.web.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.sqm.TemporalUnit;
import org.springframework.security.authentication.AuthenticationManager;
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
    public JwtResponse login(JwtRequest loginRequest) {
        JwtResponse jwtResponse = new JwtResponse();//создаем ответ на запрос

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));//отравляет на JwtUserDetailsService

        Instant validity=Instant.now()
                .plus(jwtProperties.getAccess(), ChronoUnit.MINUTES);

        User user = userService.getByUsername(loginRequest.getUsername()); //А если нет такого юзера? то выдается ошибка выше в методе
        jwtResponse.setId(user.getId());
        jwtResponse.setName(user.getName());
        jwtResponse.setExpiration(validity.toString());
        jwtResponse.setAccessToken(jwtTokenProvider.createAccessToken(user.getId(), user.getUsername(), user.getRoles(), validity));

        jwtResponse.setRefreshToken(jwtTokenProvider.createRefreshToken(user.getId(), user.getUsername()));

        return jwtResponse;
    }

    @Override
    public JwtResponse refresh(String refreshToken) {
        Instant validity=Instant.now().atZone(ZoneId.of("UTC")).toInstant();
        Instant result = validity.plus(jwtProperties.getAccess(), ChronoUnit.MINUTES);
        return jwtTokenProvider.refreshUserToken(refreshToken, result);
    }

    @Override
    public void logout(String refreshToken) {
        jwtTokenProvider.logoutUser(refreshToken);
    }
}