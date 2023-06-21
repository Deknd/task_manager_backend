package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.exception.AccessDeniedException;
import com.knd.developer.task_manager.domain.exception.MethodArgumentNotValidException;
import com.knd.developer.task_manager.domain.exception.ResourceNotFoundException;
import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.repository.UserRepository;
import com.knd.developer.task_manager.service.TokensService;
import com.knd.developer.task_manager.service.props.PatternString;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import com.knd.developer.task_manager.web.dto.user.request.UserCreateRequestDto;
import com.knd.developer.task_manager.web.dto.user.request.UserDeleteRequestDto;
import com.knd.developer.task_manager.web.dto.user.request.UserUpdateRequestDto;
import com.knd.developer.task_manager.web.dto.user.response.UserResponseDto;
import com.knd.developer.task_manager.web.mappers.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private final Pattern FORBIDDEN_JS_CHARS_PATTERN = Pattern.compile("[<>&\']");
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private PatternString pattern;
    @Mock
    private  TokensService tokensService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TaskMapper taskMapper;


    @Test
    void getById_ShouldCallUserRepository_ReturnUserOrResourceNotFoundException() {
        User user = mock(User.class);
        Long id = id_l();

        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));

        User result = userService.getById(id);

        assertEquals(user, result);
        verify(userRepository).findById(eq(id));
        assertThrows(ResourceNotFoundException.class, () -> userService.getById(id_l()));
    }

    @Test
    void getByUsername_ShouldCallUserRepository_ReturnUserOrResourceNotFoundException() {
        User user = mock(User.class);
        String username = "Username";

        when(userRepository.findByUsername(eq(username))).thenReturn(Optional.of(user));

        User result = userService.getByUsername(username);

        assertEquals(user, result);
        verify(userRepository).findByUsername(eq(username));
        assertThrows(ResourceNotFoundException.class, () -> userService.getByUsername("C8uN1qJY"));
    }

    @Test
    void update_ShouldValidationOldPasswordAndUpdateUser() {
        String oldPassword = "OldPassword";
        String newPassword = "NewPassword";
        Long id = id_l();
        PasswordEncoder pasEnc = new BCryptPasswordEncoder();
        List<Task> tasks = mock(List.class);
        List<TaskDto> tasksDto = mock(List.class);

        UserUpdateRequestDto userUpdate = new UserUpdateRequestDto();
        userUpdate.setId(id);
        userUpdate.setOldPassword(oldPassword);
        userUpdate.setNewName("NewUser");
        userUpdate.setNewUsername("NewEmailUser@mail.ru");
        userUpdate.setNewPassword(newPassword);

        User user = new User();
        user.setId(id);
        user.setTasks(tasks);
        user.setPassword(pasEnc.encode(oldPassword));

        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenAnswer(invocationOnMock -> {
            CharSequence charSequence = invocationOnMock.getArgument(0);
            String encodePassword = invocationOnMock.getArgument(1);
            return pasEnc.matches(charSequence, encodePassword);
        });
        when(passwordEncoder.encode(any(String.class))).thenAnswer(invocationOnMock -> {
            String code = invocationOnMock.getArgument(0);
            return pasEnc.encode(code);
        });
        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(pattern.getEMAIL_PATTERN()).thenReturn(EMAIL_PATTERN);
        when(taskMapper.toDto(any(List.class))).thenReturn(tasksDto);


        UserResponseDto result = userService.update(userUpdate);

        verify(userRepository).update(any(User.class));
        assertNotNull(result);

        assertEquals(id, result.getId());
        assertEquals(userUpdate.getNewName(), result.getName());

    }

    @Test
    void update_ShouldNoUpdateUserIfNoData() {
        String oldPassword = "OldPassword";
        String newPassword = "NewPassword";
        Long id = id_l();
        PasswordEncoder pasEnc = new BCryptPasswordEncoder();
        List<Task> tasks = mock(List.class);
        List<TaskDto> tasksDto = mock(List.class);

        UserUpdateRequestDto userUpdate = new UserUpdateRequestDto();
        userUpdate.setId(id);
        userUpdate.setOldPassword(oldPassword);
        userUpdate.setNewName(null);
        userUpdate.setNewUsername(null);
        userUpdate.setNewPassword(null);

        User user = new User();
        user.setId(id);
        user.setTasks(tasks);
        user.setPassword(pasEnc.encode(oldPassword));

        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenAnswer(invocationOnMock -> {
            CharSequence charSequence = invocationOnMock.getArgument(0);
            String encodePassword = invocationOnMock.getArgument(1);
            return pasEnc.matches(charSequence, encodePassword);
        });


        UserResponseDto result = userService.update(userUpdate);

        verify(userRepository, never()).update(any(User.class));
        assertNotNull(result);

        assertEquals(id, result.getId());
        assertEquals(userUpdate.getNewName(), result.getName());

    }

    @Test
    void update_ShouldCallExceptionToAccessDeniedException() {
        String oldPassword = "OldPassword";
        String newPassword = "NewPassword";
        Long id = id_l();
        PasswordEncoder pasEnc = new BCryptPasswordEncoder();

        UserUpdateRequestDto user1 = new UserUpdateRequestDto();
        user1.setId(id);
        user1.setOldPassword("0fB");
        user1.setNewName("NewUser");
        user1.setNewUsername("NewEmailUser@mail.ru");
        user1.setNewPassword(newPassword);

        UserUpdateRequestDto user2 = new UserUpdateRequestDto();
        user2.setId(id);
        user2.setOldPassword(oldPassword);
        user2.setNewName("NewUser <['");
        user2.setNewUsername("NewEmailUser@mail.ru");
        user2.setNewPassword(newPassword);

        UserUpdateRequestDto user3 = new UserUpdateRequestDto();
        user3.setId(id);
        user3.setOldPassword(oldPassword);
        user3.setNewName("NewUser");
        user3.setNewUsername("NewEmailUsermail.ru");
        user3.setNewPassword(newPassword);

        User user = new User();
        user.setId(id);
        user.setPassword(pasEnc.encode(oldPassword));

        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenAnswer(invocationOnMock -> {
            CharSequence charSequence = invocationOnMock.getArgument(0);
            String encodePassword = invocationOnMock.getArgument(1);
            return pasEnc.matches(charSequence, encodePassword);
        });

        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(pattern.getEMAIL_PATTERN()).thenReturn(EMAIL_PATTERN);


        assertThrows(AccessDeniedException.class, () -> userService.update(user1), " не верный password");
        assertThrows(MethodArgumentNotValidException.class, () -> userService.update(user2), "Не верный name");
        assertThrows(MethodArgumentNotValidException.class, () -> userService.update(user3), "Не верный email");


    }

    @Test
    void update_ShouldUpdateUserWhereOneElement() {
        String oldPassword = "OldPassword";
        String newPassword = "NewPassword";
        Long id = id_l();
        PasswordEncoder pasEnc = new BCryptPasswordEncoder();
        List<Task> tasks = mock(List.class);
        List<TaskDto> tasksDto = mock(List.class);

        UserUpdateRequestDto userUpdateName = new UserUpdateRequestDto();
        userUpdateName.setId(id);
        userUpdateName.setOldPassword(oldPassword);
        userUpdateName.setNewName("NewUser");
        userUpdateName.setNewUsername(null);
        userUpdateName.setNewPassword(null);

        UserUpdateRequestDto userUpdateUsername = new UserUpdateRequestDto();
        userUpdateUsername.setId(id);
        userUpdateUsername.setOldPassword(oldPassword);
        userUpdateUsername.setNewName(null);
        userUpdateUsername.setNewUsername("NewEmailUser@mail.ru");
        userUpdateUsername.setNewPassword(null);

        UserUpdateRequestDto userUpdatePassword = new UserUpdateRequestDto();
        userUpdatePassword.setId(id);
        userUpdatePassword.setOldPassword(oldPassword);
        userUpdatePassword.setNewName(null);
        userUpdatePassword.setNewUsername(null);
        userUpdatePassword.setNewPassword(newPassword);

        User user = new User();
        user.setId(id);
        user.setTasks(tasks);
        user.setPassword(pasEnc.encode(oldPassword));

        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenAnswer(invocationOnMock -> {
            CharSequence charSequence = invocationOnMock.getArgument(0);
            String encodePassword = invocationOnMock.getArgument(1);
            return pasEnc.matches(charSequence, encodePassword);
        });
        when(passwordEncoder.encode(any(String.class))).thenAnswer(invocationOnMock -> {
            String code = invocationOnMock.getArgument(0);
            return pasEnc.encode(code);
        });
        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(pattern.getEMAIL_PATTERN()).thenReturn(EMAIL_PATTERN);
        when(taskMapper.toDto(any(List.class))).thenReturn(tasksDto);


        userService.update(userUpdateName);

        assertEquals(userUpdateName.getNewName(), user.getName());
        assertNull(user.getUsername());
        assertTrue(pasEnc.matches(oldPassword, user.getPassword()));

        userService.update(userUpdateUsername);

        assertEquals(userUpdateName.getNewName(), user.getName());
        assertEquals(userUpdateUsername.getNewUsername(), user.getUsername());
        assertTrue(pasEnc.matches(oldPassword, user.getPassword()));

        userService.update(userUpdatePassword);

        assertEquals(userUpdateName.getNewName(), user.getName());
        assertEquals(userUpdateUsername.getNewUsername(), user.getUsername());
        assertTrue(pasEnc.matches(newPassword, user.getPassword()));
        verify(userRepository, times(3)).update(any(User.class));
    }

    @Test
    void create_ShouldCreateUserAndSave() {
        UserCreateRequestDto requestDto = new UserCreateRequestDto();
        requestDto.setName("Name");
        requestDto.setUsername("username@server.ru");
        requestDto.setPassword("newpassword");

        PasswordEncoder pasEnc = new BCryptPasswordEncoder();

        when(passwordEncoder.encode(any(String.class))).thenAnswer(invocationOnMock -> {
            String code = invocationOnMock.getArgument(0);
            return pasEnc.encode(code);
        });
        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(pattern.getEMAIL_PATTERN()).thenReturn(EMAIL_PATTERN);
        doAnswer(invocation -> {
            User arg=invocation.getArgument(0);
            arg.setId(id_l());
            return null;
        }).when(userRepository).create(any(User.class));

        userService.create(requestDto);

        ArgumentCaptor<User> requestCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).create(requestCaptor.capture());
        User user = requestCaptor.getValue();
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(requestDto.getName(), user.getName());
        assertEquals(requestDto.getUsername(), user.getUsername());
        assertTrue(pasEnc.matches(requestDto.getPassword(),user.getPassword()));
        verify(userRepository).insertUserRole(eq(user.getId()),eq(Role.ROLE_USER.name()));

    }

    @Test
    void create_ShouldCallExceptionIfUserExists(){
        UserCreateRequestDto userDto = mock(UserCreateRequestDto.class);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        assertThrows(IllegalStateException.class, () -> userService.create(userDto));
    }
    @Test
    void create_ShouldDataIsNull(){
        UserCreateRequestDto user1 = new UserCreateRequestDto();
        user1.setName(null);
        user1.setUsername("username@server.ru");
        user1.setPassword("newpassword");
        UserCreateRequestDto user2 = new UserCreateRequestDto();
        user2.setName("Name");
        user2.setUsername(null);
        user2.setPassword("newpassword");
        UserCreateRequestDto user3 = new UserCreateRequestDto();
        user3.setName("Name");
        user3.setUsername("username@server.ru");
        user3.setPassword(null);
        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(pattern.getEMAIL_PATTERN()).thenReturn(EMAIL_PATTERN);


        assertThrows(IllegalStateException.class, () -> {userService.create(user1);});
        assertThrows(IllegalStateException.class, () -> {userService.create(user2);});
        assertThrows(IllegalStateException.class, () -> {userService.create(user3);});

    }

    @Test
    void delete_ShouldValidationPasswordAndDeleteUser(){


        Long id = id_l();




        userService.delete(id);

        verify(userRepository).delete(id);
    }
//    @Test
//    void delete_ShouldThrowsExceptionWhenPasswordWrong(){
//        PasswordEncoder pasEnc = new BCryptPasswordEncoder();
//        User user = new User();
//        String pass = "password";
//        user.setPassword(pasEnc.encode(pass));
//
//        Long id = id_l();
//        UserDeleteRequestDto password = new UserDeleteRequestDto();
//        password.setPassword("Hk7");
//
//        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));
//        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenAnswer(invocationOnMock -> {
//            CharSequence charSequence = invocationOnMock.getArgument(0);
//            String encodePassword = invocationOnMock.getArgument(1);
//            return pasEnc.matches(charSequence, encodePassword);
//        });
//
//        assertThrows(AccessDeniedException.class, () -> userService.delete(id, password));
//
//        verify(userRepository, never()).delete(id);
//    }
    @Test
    void logout_ShouldUseJwtTokenProvider() {
        Long id = 898L;
        userService.logout(id);

        verify(tokensService).deleteToken(id.toString());
    }




    private Long id_l() {
        UUID uuid = UUID.randomUUID();
        Long idLong = Math.abs(uuid.getMostSignificantBits());
        return idLong;
    }

}