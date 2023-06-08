package com.knd.developer.task_manager.web.security;

import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Нужно скорее всего переделать этот класс, так как мне кажется он лишний
 */
@Component
public class JwtEntityFactory {

    /**
     * Создает из User объект для Spring security в виде JwtEntity
     * @param user - Пользователь, которого нужно переделать в JwtEntity
     * @return - JwtEntity полностью заолненого
     */

    public JwtEntity create(User user){
        return new JwtEntity(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getPassword(),
                mapToGrantedAuthorities(user.getRoles())
        );
    }

    /**
     * Маппит List<Role> в List<GrantedAuthority>
     * @param roles - List<Role> пользователя
     * @return - возвращает List<GrantedAuthority> ролей для Spring security
     */
    private  List<GrantedAuthority> mapToGrantedAuthorities(Set<Role> roles){
        return roles.stream()
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
