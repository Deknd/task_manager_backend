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
import com.knd.developer.task_manager.web.dto.user.request.UserUpdateRequestDto;
import com.knd.developer.task_manager.web.dto.user.response.UserResponseDto;
import com.knd.developer.task_manager.web.mappers.TaskMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static Yaml yaml;


    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private PatternString pattern;
    @Mock
    private TokensService tokensService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private TaskMapper taskMapper;
    private static Pattern FORBIDDEN_JS_CHARS_PATTERN;
    private static Pattern EMAIL_PATTERN;

    //достает данные из application.yml нужные для тестов
    @BeforeAll
    static void init() throws FileNotFoundException {
        InputStream stream = new FileInputStream("src/main/resources/application.yml");
        yaml = new Yaml();
        Map<String, Object> data = yaml.load(stream);
        Object ob = data.get("entity");
        if (ob instanceof Map) {
            Map<String, Object> entity = (Map<String, Object>) ob;
            Object pattern = entity.get("pattern");
            if (pattern instanceof Map) {
                Map<String, String> patternProps = (Map<String, String>) pattern;
                EMAIL_PATTERN = Pattern.compile(patternProps.get("email_pattern"));
                FORBIDDEN_JS_CHARS_PATTERN = Pattern.compile(patternProps.get("forbidden_js_chars_pattern"));
            }

        }

    }

    //Делает запрос в БД, если пользователь существует, вернет полностью заполненного пользователя
    //Если пользователя нет, выкинет исключение ResourceNotFoundException
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

    //Делает запрос в БД, если пользователь существует, вернет полностью заполненного пользователя
    //Если пользователя нет, выкинет исключение ResourceNotFoundException
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

    //Получает из БД старого пользователя, сравнивает пароль полученный от пользователя(oldPassword) с паролем из БД.
    //Проводит валидацию данных. Сохраняет измененного пользователя в БД. Возвращает пользователя в виде UserResponseDto.
    @Test
    void update_ShouldValidationOldPasswordAndUpdateUser() {
        PasswordEncoder pasEnc = new BCryptPasswordEncoder();
        Long id = id_l();

        String oldPassword = "OldPassword";
        String newPassword = "NewPassword";
        List<Task> tasks = mock(List.class);
        List<TaskDto> tasksDto = mock(List.class);

        UserUpdateRequestDto userUpdate = UserUpdateRequestDto.builder()
                .id(id)
                .oldPassword(oldPassword)
                .newName("NewUser")
                .newUsername("NewEmailUser@mail.ru")
                .newPassword(newPassword)
                .build();


        User user = User.builder()
                .id(id)
                .tasks(tasks)
                .password(pasEnc.encode(oldPassword))
                .build();


        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenAnswer(invocationOnMock -> {
            CharSequence charSequence = invocationOnMock.getArgument(0);
            String encodePassword = invocationOnMock.getArgument(1);
            return pasEnc.matches(charSequence, encodePassword);
        });

        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(pattern.getEMAIL_PATTERN()).thenReturn(EMAIL_PATTERN);
        when(passwordEncoder.encode(any(String.class))).thenAnswer(invocationOnMock -> {
            String code = invocationOnMock.getArgument(0);
            return pasEnc.encode(code);
        });

        when(taskMapper.toDto(any(List.class))).thenReturn(tasksDto);


        UserResponseDto result = userService.update(userUpdate);


        ArgumentCaptor<User> requestCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).update(requestCaptor.capture());
        User requestUser = requestCaptor.getValue();

        assertNotNull(requestUser);
        assertEquals(userUpdate.getNewUsername(), requestUser.getUsername());
        assertTrue(pasEnc.matches(newPassword, requestUser.getPassword()));
        assertNotNull(result);

        assertEquals(id, result.getId());
        assertEquals(userUpdate.getNewName(), result.getName());

    }

    //Выкидывает исключение, так как в БД нет пользователя под данным id(UserUpdateRequestDto.id)
    @Test
    void update_ShouldTrowsAccessDeniedException() {
        UserUpdateRequestDto user = mock(UserUpdateRequestDto.class);

        when(userRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(AccessDeniedException.class, () -> userService.update(user));
    }

    //Если oldPassword из UserUpdateRequestDto не совпадает с паролем из БД, выкидывает исключение AccessDeniedException
    //Если oldPassword равен null, выкидывает исключение AccessDeniedException
    //Если в newName из UserUpdateRequestDto есть запрещенные символы(указаны в application.yml: entity.pattern.forbidden_js_chars_pattern), выкидывает исключение MethodArgumentNotValidException
    //Если в newUsername данные не соответствуют email(паттерн указаны в application.yml: entity.pattern.email_pattern), выкидывает исключение MethodArgumentNotValidException
    @Test
    void update_ShouldCallExceptionToAccessDeniedException() {
        String oldPassword = "OldPassword";
        String newPassword = "NewPassword";
        Long id = id_l();
        PasswordEncoder pasEnc = new BCryptPasswordEncoder();

        UserUpdateRequestDto user1 = UserUpdateRequestDto.builder()
                .id(id)
                .oldPassword("0fB")
                .newName("NewUser")
                .newUsername("NewEmailUser@mail.ru")
                .newPassword(newPassword)
                .build();

        UserUpdateRequestDto user2 = UserUpdateRequestDto.builder()
                .id(id)
                .newName("NewUser")
                .newUsername("NewEmailUser@mail.ru")
                .newPassword(newPassword)
                .build();

        UserUpdateRequestDto user3 = UserUpdateRequestDto.builder()
                .id(id)
                .oldPassword(oldPassword)
                .newName("NewUser <['")
                .newUsername("NewEmailUser@mail.ru")
                .newPassword(newPassword)
                .build();


        UserUpdateRequestDto user4 = UserUpdateRequestDto.builder()
                .id(id)
                .oldPassword(oldPassword)
                .newName("NewUser")
                .newUsername("NewEmailUsermail.ru")
                .newPassword(newPassword)
                .build();


        User user = User.builder()
                .id(id)
                .password(pasEnc.encode(oldPassword))
                .build();


        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenAnswer(invocationOnMock -> {
            CharSequence charSequence = invocationOnMock.getArgument(0);
            String encodePassword = invocationOnMock.getArgument(1);
            return pasEnc.matches(charSequence, encodePassword);
        });

        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(pattern.getEMAIL_PATTERN()).thenReturn(EMAIL_PATTERN);


        assertThrows(AccessDeniedException.class, () -> userService.update(user1), " не верный password");
        assertThrows(AccessDeniedException.class, () -> userService.update(user2), " не верный password");

        assertThrows(MethodArgumentNotValidException.class, () -> userService.update(user3), "Не верный name");
        assertThrows(MethodArgumentNotValidException.class, () -> userService.update(user4), "Не верный email");


    }

    //Если данных(UserUpdateRequestDto: newName, newPassword, newUsername) нет, то вернутся данные пользователя из БД
    @Test
    void update_ShouldNoUpdateUserIfNoData() {
        String oldPassword = "OldPassword";

        Long id = id_l();
        PasswordEncoder pasEnc = new BCryptPasswordEncoder();
        List<Task> tasks = mock(List.class);

        List<TaskDto> tasksDto = mock(List.class);

        UserUpdateRequestDto userUpdate = UserUpdateRequestDto.builder()
                .id(id)
                .oldPassword(oldPassword)
                .build();


        User user = User.builder()
                .id(id)
                .tasks(tasks)
                .password(pasEnc.encode(oldPassword))
                .build();


        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenAnswer(invocationOnMock -> {
            CharSequence charSequence = invocationOnMock.getArgument(0);
            String encodePassword = invocationOnMock.getArgument(1);
            return pasEnc.matches(charSequence, encodePassword);
        });

        when(taskMapper.toDto(any(List.class))).thenReturn(tasksDto);

        UserResponseDto result = userService.update(userUpdate);

        verify(userRepository, never()).update(any(User.class));
        assertNotNull(result);

        assertEquals(id, result.getId());
        assertEquals(userUpdate.getNewName(), result.getName());

    }


    //Можно обновлять данные пользователя( name, username, password) по одному полю, для этого нужно отправить UserUpdateRequestDto(id и oldPassword - обязательны) с этим полем.
    @Test
    void update_ShouldUpdateUserWhereOneElement() {
        String oldPassword = "OldPassword";
        String newPassword = "NewPassword";
        Long id = id_l();
        PasswordEncoder pasEnc = new BCryptPasswordEncoder();
        List<Task> tasks = mock(List.class);
        List<TaskDto> tasksDto = mock(List.class);

        UserUpdateRequestDto userUpdateName = UserUpdateRequestDto.builder()
                .id(id)
                .oldPassword(oldPassword)
                .newName("New User")
                .build();


        UserUpdateRequestDto userUpdateUsername = UserUpdateRequestDto.builder()
                .id(id)
                .oldPassword(oldPassword)
                .newUsername("NewEmailUser@mail.ru")
                .build();


        UserUpdateRequestDto userUpdatePassword = UserUpdateRequestDto.builder()
                .id(id)
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .build();


        User user = User.builder()
                .id(id)
                .tasks(tasks)
                .password(pasEnc.encode(oldPassword))
                .build();


        when(userRepository.findById(eq(id))).thenReturn(Optional.of(user));

        when(passwordEncoder.matches(any(CharSequence.class), any(String.class))).thenAnswer(invocationOnMock -> {
            CharSequence charSequence = invocationOnMock.getArgument(0);
            String encodePassword = invocationOnMock.getArgument(1);
            return pasEnc.matches(charSequence, encodePassword);
        });

        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(pattern.getEMAIL_PATTERN()).thenReturn(EMAIL_PATTERN);
        when(passwordEncoder.encode(any(String.class))).thenAnswer(invocationOnMock -> {
            String code = invocationOnMock.getArgument(0);
            return pasEnc.encode(code);
        });

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

    //Проводит валидацию данных(name, username), сохраняет нового пользователя в БД
    @Test
    void create_ShouldCreateUserAndSave() {
        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .name("Name")
                .username("username@server.ru")
                .password("newpassword")
                .build();

        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(pattern.getEMAIL_PATTERN()).thenReturn(EMAIL_PATTERN);
        PasswordEncoder pasEnc = new BCryptPasswordEncoder();

        when(passwordEncoder.encode(any(String.class))).thenAnswer(invocationOnMock -> {
            String code = invocationOnMock.getArgument(0);
            return pasEnc.encode(code);
        });

        doAnswer(invocation -> {
            User arg = invocation.getArgument(0);
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
        assertTrue(pasEnc.matches(requestDto.getPassword(), user.getPassword()));

        verify(userRepository).insertUserRole(eq(user.getId()), eq(Role.ROLE_USER.name()));

    }


    //Если какое-нибудь поле в UserCreateRequestDto будет null, то выкинет исключение IllegalStateException
    @Test
    void create_ShouldDataIsNull() {
        UserCreateRequestDto user1 = UserCreateRequestDto.builder()
                .username("username@server.ru")
                .password("newpassword")
                .build();

        UserCreateRequestDto user2 = UserCreateRequestDto.builder()
                .name("Name")
                .password("newpassword")
                .build();

        UserCreateRequestDto user3 = UserCreateRequestDto.builder()
                .name("Name")
                .username("username@server.ru")
                .build();


        assertThrows(IllegalStateException.class, () -> {
            userService.create(user1);
        });
        assertThrows(IllegalStateException.class, () -> {
            userService.create(user2);
        });
        assertThrows(IllegalStateException.class, () -> {
            userService.create(user3);
        });

    }

    //Если существует пользователь с данным username, то выкидывается исключение IllegalStateException
    @Test
    void create_ShouldTrowsExceptionIllegalStateException_BecauseSuchUserExists() {
        UserCreateRequestDto requestDto = UserCreateRequestDto.builder()
                .name("Name")
                .username("username@server.ru")
                .password("newpassword")
                .build();
        when(userRepository.findByUsername(eq(requestDto.getUsername()))).thenReturn(Optional.of(mock(User.class)));
        assertThrows(IllegalStateException.class, () -> userService.create(requestDto));

    }
    //Если данные не проходят валидацию(в name используются запрещенные символы(указаны в application.yml: entity.pattern.forbidden_js_chars_pattern),
    // username не соответствует стандарту email(паттерн указаны в application.yml: entity.pattern.email_pattern),
    //то выкидывает исключение MethodArgumentNotValidException.
    @Test
    void create_ShouldTrowsExceptionMethodArgumentNotValidException_BecauseDataNotValidate(){
        UserCreateRequestDto failedName = UserCreateRequestDto.builder()
                .name("Name['>")
                .username("username@server.ru")
                .password("new_password")
                .build();
        UserCreateRequestDto failedUsername = UserCreateRequestDto.builder()
                .name("Name")
                .username("username_server.ru")
                .password("new_password")
                .build();

        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(pattern.getEMAIL_PATTERN()).thenReturn(EMAIL_PATTERN);

        assertThrows(MethodArgumentNotValidException.class, ()-> userService.create(failedName));
        assertThrows(MethodArgumentNotValidException.class, ()-> userService.create(failedUsername));
    }

    //Делает запрос в БД на удаление данных пользователя по указанному id.
    @Test
    void delete_ShouldValidationPasswordAndDeleteUser() {


        Long id = id_l();


        userService.delete(id);

        verify(tokensService).deleteToken(eq(id.toString()));
        verify(userRepository).delete(eq(id));
    }

    //Делает запрос в БД на удаление token по указанному id
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