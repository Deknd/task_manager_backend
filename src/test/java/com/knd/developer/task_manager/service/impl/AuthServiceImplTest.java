package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.service.UserService;
import com.knd.developer.task_manager.service.props.JwtProperties;
import com.knd.developer.task_manager.web.dto.auth.JwtRequest;
import com.knd.developer.task_manager.web.dto.auth.RefreshRequest;
import com.knd.developer.task_manager.web.dto.auth.ResponseAuthUser;
import com.knd.developer.task_manager.web.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtProperties jwtProperties;
    @Mock
    private UserService userService;
    @Mock
    private JwtTokenProvider tokenProvider;

    @Test
    void login_ShouldAuthenticationAndReturnJwtResponse() {
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        JwtRequest request = new JwtRequest("Username", "Password");
        User user = new User();
        user.setId(1L);
        user.setName("User");
        user.setUsername("Username");
        user.setPassword("Password");
        user.setRoles(Set.of(Role.ROLE_USER));
        user.setTasks(List.of(task1, task2));
        when(userService.getByUsername(request.getUsername())).thenReturn(user);
        when(jwtProperties.getAccess()).thenReturn(15L);
        when(tokenProvider.createAccessToken(eq(user.getId()), eq(user.getUsername()), eq(user.getRoles()), any(Instant.class))).thenReturn("Access Token");
        when(tokenProvider.createRefreshToken(user.getId(), user.getUsername())).thenReturn("Refresh token");

        ResponseAuthUser result = authService.login(request);

        assertNotNull(result);
        assertEquals(user.getId(),result.getId());
        assertEquals(user.getName(),result.getName());
        assertEquals("Access Token", result.getAccessToken());
        assertEquals("Refresh token", result.getRefreshToken());
        assertTrue(Instant.parse(result.getExpiration()).isAfter(Instant.now()));
        assertEquals(user.getTasks(), result.getTasks());
        verify(authenticationManager).authenticate(any());
        verify(jwtProperties).getAccess();
    }
    @Test
    void login_ShouldTrowsExceptionAuthentication(){
        JwtRequest jwtRequest = mock(JwtRequest.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(InternalAuthenticationServiceException.class);

        assertThrows(BadCredentialsException.class,() -> authService.login(jwtRequest));

    }
    @Test
    void refresh_ShouldReturnResponseAuthUser(){
        RefreshRequest request = new RefreshRequest("Refresh token");
        ResponseAuthUser responseAuthUser = mock(ResponseAuthUser.class);
        responseAuthUser.setRefreshToken("New Refresh token");

        ArgumentCaptor<Instant> requestCaptor = ArgumentCaptor.forClass(Instant.class);
        when(tokenProvider.refreshUserToken(eq(request.getRefreshToken()), requestCaptor.capture())).thenReturn(responseAuthUser);
        when(jwtProperties.getAccess()).thenReturn(15L);


        ResponseAuthUser result = authService.refresh(request);

        Instant capturedArgument = requestCaptor.getValue();
        assertTrue(capturedArgument.isAfter(Instant.now()));
        assertNotEquals(request.getRefreshToken(),result.getRefreshToken());
    }

    @Test
    void logout_ShouldUseJwtTokenProvider(){
        String refreshToken = "Refresh token";
        doNothing().when(tokenProvider).logoutUser(eq(refreshToken));

        authService.logout(refreshToken);

        verify(tokenProvider).logoutUser(refreshToken);
    }


}