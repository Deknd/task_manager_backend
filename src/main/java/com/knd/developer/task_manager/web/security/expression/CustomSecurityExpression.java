package com.knd.developer.task_manager.web.security.expression;

import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.service.TaskService;
import com.knd.developer.task_manager.web.security.JwtEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;

/**
 * Кастамная проверка прав доступа пользователя к данным
 */
@Service("customSecurityExpression")
@RequiredArgsConstructor
public class CustomSecurityExpression {
    private final TaskService taskService;

    /**
     * Должен проверить, разрешен ли пользователю который авторизован в системе, доступ к запрашиваемым данным
     * @param id - айди пользователя, к данным которого запрашивают доступ
     * @return - true, если доступ разрешен, иначе false
     */
    public boolean canAccessUser(Long id){
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        JwtEntity user = (JwtEntity) authentication.getPrincipal();
        Long userId = user.getId();

        return userId.equals(id); //|| hasAnyRole(authentication, Role.ROLE_ADMIN);
    }

    /**
     * Проверяет, доступен ли для авторизованного пользователя данный таск
     * @param taskId - айди таска, которого надо проверить
     * @return - Возвращает true, если таска принадлежит пользователю, иначе false
     */
    public boolean canAccessTask(Long taskId){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();

        JwtEntity user = (JwtEntity) authentication.getPrincipal();

        Long id= user.getId();

        return taskService.isTaskOwner(id, taskId);
    }

    /**
     * Проверяет, является ли пользователь админом
     * @param authentication - Authentication пользователя, который сейчас аутентифицирован в системе
     * @param roles - Role с которыми надо сравнить роли пользователя
     * @return - Возвращает true, если пользователь является админом, иначе false
     */
    private boolean hasAnyRole(Authentication authentication, Role... roles){
        for(Role role: roles){
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
            if(authentication.getAuthorities().contains(authority)){
                return true;
            }
        }
        return false;
    }

}
