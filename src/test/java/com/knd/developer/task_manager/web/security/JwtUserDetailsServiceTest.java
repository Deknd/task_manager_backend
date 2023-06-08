package com.knd.developer.task_manager.web.security;

import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUserDetailsServiceTest {

    @InjectMocks
    private JwtUserDetailsService detailsService;
    @Mock
    private JwtEntityFactory entityFactory;
    @Mock
    private UserService userService;

    @Test
    void loadUserByUsername_ShouldLoadUser(){
        User user = mock(User.class);
        UserDetails userDetails = mock(JwtEntity.class);
        when(userService.getByUsername(any(String.class))).thenReturn(user);
        when(entityFactory.create(eq(user))).thenReturn((JwtEntity) userDetails);

        UserDetails result = detailsService.loadUserByUsername("zZldrK");

        assertNotNull(result);
    }
}