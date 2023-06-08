package com.knd.developer.task_manager.service;

import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.web.dto.user.request.UserCreateRequestDto;
import com.knd.developer.task_manager.web.dto.user.request.UserDeleteRequestDto;
import com.knd.developer.task_manager.web.dto.user.request.UserUpdateRequestDto;
import com.knd.developer.task_manager.web.dto.user.response.UserResponseDto;

import java.util.List;


public interface UserService {
    /**
     * Запрашивает в UserRepository пользователя по даннму айди, если пользователя нет, выбрасывает ошибку: ResourceNotFoundException
     * @param id - айди пользователя, которого ищут
     * @return - возврощает полностью заполненого пользователя
     */
    User getById(Long id);
    /**
     * Запрашивает в UserRepository пользователя по даннму имени пользователя, если пользователя нет, выбрасывает ошибку: ResourceNotFoundException
     * @param username - юзер найм пользователя, которого надо вернуть
     * @return возврощает полностью заполненого пользователя
     */
    User getByUsername(String username);
//    UserResponseDto getUser(Long id);

    /**
     * Обновляет всего пользователя или его отдельные данные(name, username, password)
     * @param user - принимает UserUpdateRequestDto, в котором обязательно должен быть id - пользователя и текщий пароль пользователя
     * @return - возврощает полностью заполненого юзера для клиента
     */
    UserResponseDto update(UserUpdateRequestDto user);
    /**
     * Проверяет, существует ли под данным email пользователь, если нет, то проводит валидацию вводимых данных и сохраняет нового пользователя в базу данных
     * @param user - UserCreateRequestDto должен быть полностью заполнен, если не будет хоть одних данных, выдаст эксепшен(IllegalStateException)
     */
    void create(UserCreateRequestDto user);
    /**
     * Проверяет текущий пароль пользователя, если он верен, удаляет пользователя из базы данных
     * @param id - (Long) айди пользователя, которого нужно удалить
     * @param password - UserDeleteRequestDto содержащее единственное поле с текущим паролем пользователя - password (не может быть null)
     */
    void delete(Long id, UserDeleteRequestDto password);
    /**
     * Удаляет рефреш токен из памяти системы, тем самым делает не валидными все токены доступа
     * @param id - айдипользователя
     */
    void logout(Long id);
}
