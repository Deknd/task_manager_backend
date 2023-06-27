package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.service.UserService;
import com.knd.developer.task_manager.service.props.JwtProperties;
import com.knd.developer.task_manager.web.dto.auth.LoginRequest;
import com.knd.developer.task_manager.web.dto.auth.RefreshRequest;
import com.knd.developer.task_manager.web.dto.user.response.UserAndTokenResponseDto;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import com.knd.developer.task_manager.web.mappers.TaskMapper;
import com.knd.developer.task_manager.web.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
    private TaskMapper taskMapper;
    @Mock
    private JwtTokenProvider tokenProvider;

    //аутентифицирует пользователя по полученным данным
    @Test
    void login_ShouldAuthenticationAndReturnJwtResponse() {
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        List<TaskDto> listDto = mock(List.class);

        LoginRequest request = new LoginRequest("Username", "Password");

        User user = User.builder()
                .id(1L)
                .name("User")
                .username("username@tast.ru")
                .password("password")
                .roles(Set.of(Role.ROLE_USER))
                .tasks(List.of(task1,task2))
                .build();

        when(userService.getByUsername(request.getUsername())).thenReturn(user);
        when(jwtProperties.getAccess()).thenReturn(15L);
        when(tokenProvider.createAccessToken(eq(user.getId()), eq(user.getUsername()), eq(user.getRoles()), any(Instant.class))).thenReturn("Access Token");
        when(tokenProvider.createRefreshToken(user.getId(), user.getUsername())).thenReturn("Refresh token");
        when(taskMapper.toDto(any(List.class))).thenReturn(listDto);

        UserAndTokenResponseDto result = authService.login(request);

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals("Access Token", result.getAccessToken());
        assertEquals("Refresh token", result.getRefreshToken());
        assertTrue(Instant.parse(result.getExpiration()).isAfter(Instant.now()));
        assertEquals(listDto, result.getTasks());
        verify(authenticationManager).authenticate(any());
        verify(jwtProperties).getAccess();
    }

    //Когда данные не верны, должен выбросить ошибку BadCredentialsException
    @Test
    void login_ShouldTrowsExceptionAuthentication_BecauseDataNotCorrect() {
        LoginRequest loginRequest = mock(LoginRequest.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(InternalAuthenticationServiceException.class);

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));

    }

    //Обновляет по refreshToken токены пользователя
    @Test
    void refresh_ShouldReturnUserAndTokenResponseDto() {
        RefreshRequest request = new RefreshRequest("Refresh token");
        UserAndTokenResponseDto userAndTokenResponseDTO = mock(UserAndTokenResponseDto.class);
        //userAndTokenResponseDTO.setRefreshToken("New Refresh token");

        ArgumentCaptor<Instant> requestCaptor = ArgumentCaptor.forClass(Instant.class);
        when(tokenProvider.refreshUserToken(eq(request.getRefreshToken()), requestCaptor.capture())).thenReturn(userAndTokenResponseDTO);
        when(jwtProperties.getAccess()).thenReturn(15L);


        UserAndTokenResponseDto result = authService.refresh(request);

        Instant capturedArgument = requestCaptor.getValue();
        assertTrue(capturedArgument.isAfter(Instant.now()));
        assertNotEquals(request.getRefreshToken(), result.getRefreshToken());
    }




}