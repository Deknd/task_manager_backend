package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.exception.AccessDeniedException;
import com.knd.developer.task_manager.domain.exception.MethodArgumentNotValidException;
import com.knd.developer.task_manager.domain.exception.ResourceNotFoundException;
import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.repository.UserRepository;
import com.knd.developer.task_manager.service.TokensService;
import com.knd.developer.task_manager.service.UserService;
import com.knd.developer.task_manager.service.props.PatternString;
import com.knd.developer.task_manager.web.dto.user.request.UserCreateRequestDto;
import com.knd.developer.task_manager.web.dto.user.request.UserUpdateRequestDto;
import com.knd.developer.task_manager.web.dto.user.response.UserResponseDto;
import com.knd.developer.task_manager.web.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskMapper taskMapper;
    private final PatternString pattern;
    private final TokensService tokensService;


    /**
     * Делает запрос в БД на получение пользователя по id.
     * Если пользователя нет, выкидывает исключение ResourceNotFoundException.
     *
     * @param id (Long) идентифика́тор пользователя
     * @return - возвращает полностью заполненного пользователя
     */
    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        User result = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("Use not found.")));
        return result;
    }


    /**
     * Делает запрос в БД на получение пользователя по username.
     * Если пользователя нет, выкидывает исключение ResourceNotFoundException.
     *
     * @param username (String) Email пользователя должен подходить под данный паттерн: ^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$ (т.е. email@email.com)
     * @return (User) возвращает полностью заполненного пользователя
     */
    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    /**
     * Делает запрос вв БД и достает от туда пользователя. Сравнивает пароль полученный от пользователя(oldPassword) с паролем из БД.
     * Выполняет валидацию данных. Сохраняет измененного пользователя в БД и возвращает его в виде UserResponseDto.
     * Можно обновлять данные(name, username, password) по одному, для этого нужно отправить UserUpdateRequestDto(id и oldPassword - обязательно) с полем, которое надо изменить.
     * Если oldPassword равен null, выкидывает исключение AccessDeniedException.
     * Если пользователя под данным id(UserUpdateRequestDto.id) нет, выкидывает исключение AccessDeniedException.
     * Если oldPassword(UserUpdateRequestDto.oldPassword) не совпадает с паролем из БД, выкидывает исключение MethodArgumentNotValidException.
     * Если в newName(UserUpdateRequestDto.newName) есть запрещенные символы(указаны в application.yml: entity.pattern.forbidden_js_chars_pattern), выкидывает исключение MethodArgumentNotValidException.
     * Если в newUsername данные не соответствуют email(паттерн указан в application.yml: entity.pattern.email_pattern), выкидывает исключение MethodArgumentNotValidException.
     * Если данных(UserUpdateRequestDto: newName, newUsername, newPassword) нет, то вернутся данные пользователя из БД.
     *
     * @param user UserUpdateRequestDto присланный клиентом, обязательно должно быть заполнены id и oldPassword
     * @return UserResponseDto возвращает полностью заполненного юзера, с обновленными данными
     */
    @Override
    @Transactional
    public UserResponseDto update(UserUpdateRequestDto user) {
        Optional<User> byId = userRepository.findById(user.getId());
        if (byId.isEmpty()) throw new AccessDeniedException();

        User result = byId.get();

        if (user.getOldPassword() == null ) {
            throw new AccessDeniedException();
        } else if(!passwordEncoder.matches(user.getOldPassword(), result.getPassword())){
            throw new AccessDeniedException();
        }

        if (user.getNewName() == null && user.getNewPassword() == null && user.getNewUsername() == null)
            return mapperUserToUserResponseDto(result);

        validationDateUser(result, user.getNewName(), user.getNewUsername(), user.getNewPassword());

        userRepository.update(result);


        return mapperUserToUserResponseDto(result);
    }

    /**
     * Проверяет, существует ли под данным username в БД пользователь, если нет, то проводит валидацию вводимых данных и сохраняет нового пользователя в БД.
     * Если какое-нибудь поле в UserCreateRequestDto будет null, то выкинет исключение IllegalStateException.
     * Если в БД существует пользователь с таким же username, то выкинет исключение IllegalStateException.
     * Если данные не проходят валидацию(в name используется запрещенные символы(указаны в application.yml: entity.pattern.forbidden_js_chars_pattern) или
     * username не соответствует стандарту email(паттерн указаны в application.yml: entity.pattern.email_pattern),
     * то выкидывает исключение MethodArgumentNotValidException.
     *
     * @param user UserCreateRequestDto должен быть полностью заполнен.
     */
    @Override
    @Transactional
    public void create(UserCreateRequestDto user) {
        if (user.getName() == null || user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalStateException("Data is null");
        }

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exist");
        }

        User result = new User();
        validationDateUser(result, user.getName(), user.getUsername(), user.getPassword());

        result.setRoles(Set.of(Role.ROLE_USER));
        userRepository.create(result);
        for (Role role : result.getRoles()) {
            userRepository.insertUserRole(result.getId(), role.name());
        }
    }


    /**
     * Делает запрос в БД на удаление данных пользователя по указанному id
     *
     * @param id (Long) id пользователя, которого нужно удалить
     */
    @Override
    @Transactional
    public void delete(Long id) {
        tokensService.deleteToken(id.toString());
        userRepository.delete(id);
    }

    /**
     * Проверяет валидность данных: name - проверяет на запрещенные символы(указаны в application.yml: entity.pattern.forbidden_js_chars_pattern),
     * username - проверяет на то, что бы соответствовал Email(паттерн указаны в application.yml: entity.pattern.email_pattern).
     *
     * @param result   - User объект в который будет вписаны данные если они валидны
     * @param name     - Имя пользователя не должно содержать данные символы: [<>&']
     * @param username - Email пользователя должен подходить под данный паттерн: ^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$
     * @param password - Пароль пользователя
     */
    private void validationDateUser(User result, String name, String username, String password) {

        if (name != null) {
            if (!pattern.getFORBIDDEN_JS_CHARS_PATTERN().matcher(name).find()) {
                result.setName(name);
            } else {
                throw new MethodArgumentNotValidException("name: ", "Не верный запрос изменения Юзера, в имени используются запрещеные символы");
            }
        }
        if (username != null) {
            if (pattern.getEMAIL_PATTERN().matcher(username).matches()) {
                result.setUsername(username);
            } else {
                throw new MethodArgumentNotValidException("username: ", "Не верный запрос изменения Юзера, email не верно введен");
            }
        }
        if (password != null) {
            result.setPassword(passwordEncoder.encode(password));
        }
    }

    /**
     * Делает запрос в БД на удаление token по указанному id
     *
     * @param id (Long) пользователя
     */
    @Override
    public void logout(Long id) {
        tokensService.deleteToken(id.toString());
    }

    /**
     * Превращает User в UserResponseDto
     *
     * @param user User которого надо отправить клиенту
     * @return UserResponseDto вид User для отправки клиенту
     */
    private UserResponseDto mapperUserToUserResponseDto(User user) {
        UserResponseDto userDto = new UserResponseDto();
        if (user.getTasks() != null) {
            userDto.setListTask(taskMapper.toDto(user.getTasks()));

        }
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }
}
