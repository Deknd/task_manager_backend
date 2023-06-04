package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.domain.user.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;
@Mapper
public interface UserRepository {
    /**
     * возврощает опшен с юзером, если юзер под указаным айди существует
     * @param id id юзера тип Long
     * @return юзер будет полностью заполненый, с тасками и ролью
     */
    Optional<User> findById(Long id);


    /**
     * возврощает опшен с юзером, если юзер под указаным username существует
     * @param username username юзера тип String
     * @return юзер будет полностью заполненый, с тасками и ролью
     */
    Optional<User> findByUsername(String username);

    /**
     * Изменить можно поля: name, username, password.
     * Так как данные действия может делать только авторизованный пользователь, так что проверку на наличие айди нет
     * @param user Обязательно должен быть айди юзера
     */
    void update(User user);

    /**
     * создает пользователя
     * @param user должен быть уникальный: username
     */
    void create(User user);
    void insertUserRole(@Param("user_id") Long userId,@Param("role") String role);
//    boolean isTaskOwner(@Param("user_id") Long userId,@Param("task_id") Long taskId);

    void delete(Long id);
}
