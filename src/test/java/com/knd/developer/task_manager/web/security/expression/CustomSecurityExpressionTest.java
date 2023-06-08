package com.knd.developer.task_manager.web.security.expression;

import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.service.TaskService;
import com.knd.developer.task_manager.web.security.JwtEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomSecurityExpressionTest {

    @InjectMocks
    private CustomSecurityExpression securityExpression;

    @Mock
    private TaskService taskService;

    @Test
    void canAccessUser_ShouldCheckUserAccess() {
        Long id = 258L;
        JwtEntity user = mock(JwtEntity.class);
        Authentication authentication = mock(Authentication.class);
        when(user.getId()).thenReturn(id);
        when(authentication.getPrincipal()).thenReturn(user);

        //assertTrue(securityExpression.canAccessUser(id));

    }
    @Test
    void canAccessUser_ShouldCheckUserAccessToRoleAdmin(){
        Long id = 258L;

        GrantedAuthority roleAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");

        // Create a collection with ROLE_ADMIN authority
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(roleAdmin);

        JwtEntity user = mock(JwtEntity.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

      //  assertTrue(securityExpression.canAccessUser(id));;


    }
    @Test
    void canAccessUser_ShouldCheckUserNotAccessToRoleAdmin(){
        Long id = 258L;

        GrantedAuthority roleAdmin = new SimpleGrantedAuthority(Role.ROLE_USER.name());

        // Create a collection with ROLE_ADMIN authority
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(roleAdmin);

        JwtEntity user = mock(JwtEntity.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(user);
        when(authentication.getAuthorities()).thenAnswer(invocation -> authorities);

       // assertFalse(securityExpression.canAccessUser(id));;

    }
    @Test
    void canAccessTask_ShouldCheckAccessOfAnAuthenticationUserToTheTask(){
        Long id = 258L;
        Long taskId = 616L;
        JwtEntity user = mock(JwtEntity.class);
        Authentication authentication = mock(Authentication.class);
        when(user.getId()).thenReturn(id);
        when(authentication.getPrincipal()).thenReturn(user);
        when(taskService.isTaskOwner(eq(id), eq(taskId))).thenReturn(true);

        assertTrue(securityExpression.canAccessTask(taskId));
        assertFalse(securityExpression.canAccessTask(21321L));

    }


}

