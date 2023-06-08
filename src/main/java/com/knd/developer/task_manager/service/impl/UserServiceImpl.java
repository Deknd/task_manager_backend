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
import com.knd.developer.task_manager.web.dto.user.request.UserDeleteRequestDto;
import com.knd.developer.task_manager.web.dto.user.request.UserUpdateRequestDto;
import com.knd.developer.task_manager.web.dto.user.response.UserResponseDto;
import com.knd.developer.task_manager.web.mappers.TaskMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final TaskMapper taskMapper;
    private final PatternString pattern;
    private final TokensService tokensService;


    /**
     * Запрашивает в UserRepository пользователя по даннму айди, если пользователя нет, выбрасывает ошибку: ResourceNotFoundException
     *
     * @param id - айди пользователя, которого ищут
     * @return - возврощает полностью заполненого пользователя
     */
    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        User result = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(("Use not found.")));
        return result;
    }

//    /**
//     * Возврощает готового юзер. Возможно придется удалить, так как не используется
//     * @param id
//     * @return
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public UserResponseDto getUser(Long id){
//        return mapperUserToUserResponseDto(getById(id));
//
//    }
    /**
     * Запрашивает в UserRepository пользователя по даннму имени пользователя, если пользователя нет, выбрасывает ошибку: ResourceNotFoundException
     *
     * @param username - юзер найм пользователя, которого надо вернуть
     * @return возврощает полностью заполненого пользователя
     */

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        User result = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
        return result;
    }

    /**
     * Обновляет всего пользователя или его отдельные данные(name, username, password)
     * @param user - UserUpdateRequestDto присланый клиентом, обязательно должно быть заполнены id и oldPassword
     * @return - возврощает полностью заполненого юзера, с обновлеными данными
     */
    @Override
    @Transactional
    public UserResponseDto update(UserUpdateRequestDto user) {
        User result = getById(user.getId());

        if (user.getOldPassword()!=null && !passwordEncoder.matches(user.getOldPassword(), result.getPassword())) {
            throw new AccessDeniedException();
        }
        if(user.getNewName()==null && user.getNewPassword() == null && user.getNewUsername() == null) return mapperUserToUserResponseDto(result);

        validationDateUser(result, user.getNewName(), user.getNewUsername(), user.getNewPassword());

        userRepository.update(result);


        return mapperUserToUserResponseDto(result);
    }

    /**
     * Проверяет, существует ли под данным email пользователь, если нет, то проводит валидацию вводимых данных и сохраняет нового пользователя в базу данных
     * @param user - UserCreateRequestDto должен быть полностью заполнен, если не будет хоть одних данных, выдаст эксепшен(IllegalStateException)
     */
    @Override
    @Transactional
    public void create(UserCreateRequestDto user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("User already exist");
        }
        User result = new User();
        validationDateUser(result, user.getName(), user.getUsername(), user.getPassword());
        if(user.getName() == null || user.getUsername() == null || user.getPassword() == null){
            throw new IllegalStateException("Data is null");

        }
        result.setRoles(Set.of(Role.ROLE_USER));
        userRepository.create(result);
        for(Role role: result.getRoles()){
            userRepository.insertUserRole(result.getId(), role.name());
        }
    }


    /**
     * Проверяет текущий пароль пользователя, если он верен, удаляет пользователя из базы данных
     * @param id - (Long) айди пользователя, которого нужно удалить
     * @param password - UserDeleteRequestDto содержащее единственное поле с текущим паролем пользователя - password (не может быть null)
     */
    @Override
    @Transactional
    public void delete(Long id, UserDeleteRequestDto password) {
        User result = getById(id);
        if (!passwordEncoder.matches(password.getPassword(), result.getPassword())) {
            throw new AccessDeniedException();
        }
        userRepository.delete(id);
    }

    /**
     * Проверяет валидность данных
     * @param result - User объект в который будет вписаны данные если они валидны
     * @param name - Имя пользователя не должно содержать данные символы: [<>&']
     * @param username - Email пользователя должен подходить под данный паттерн: ^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$
     * @param password - Пароль пользователя
     */
    private void validationDateUser(User result, String name, String username, String password){
        if (name != null) {
            if (!pattern.getFORBIDDEN_JS_CHARS_PATTERN().matcher(name).find()) {
                result.setName(name);
            } else {
                throw new MethodArgumentNotValidException("name: ","Не верный запрос изменения Юзера, в имени используются запрещеные символы");
            }
        }
        if (username != null) {
            if (pattern.getEMAIL_PATTERN().matcher(username).matches()) {
                result.setUsername(username);
            } else {
                throw new MethodArgumentNotValidException("username: ","Не верный запрос изменения Юзера, email не верно введен");
            }
        }
        if (password != null) {
            result.setPassword(passwordEncoder.encode(password));
        }
    }
    /**
     * Удаляет рефреш токен из памяти системы, тем самым делает не валидными все токены доступа
     * @param id - айди пользователя
     */
    @Override
    public void logout(Long id) {
        tokensService.deleteToken(id.toString());
    }

    /**
     * Мапит User в UserResponseDto
     * @param user - User которого надо отправить клиенту
     * @return - UserResponseDto вид User для отправки клиенту
     */
    private UserResponseDto mapperUserToUserResponseDto(User user) {
        UserResponseDto userDto = new UserResponseDto();
        if(user.getTasks() !=null){
            userDto.setListTask(taskMapper.toDto(user.getTasks()));

        }
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        return userDto;
    }
}
