package com.knd.developer.task_manager.web.security;

import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.domain.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)

class JwtEntityFactoryTest {

    @InjectMocks
    private JwtEntityFactory factory;

    @Test
    void create_ShouldCreateFromUserToJwtEntity(){
        User user = new User();
        user.setId(925L);
        user.setName("blood");
        user.setUsername("season@angle.ru");
        user.setPassword("mine");
        user.setRoles(Set.of(Role.ROLE_USER));


        JwtEntity result = factory.create(user);

        assertEquals(user.getId(), result.getId());
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        for(Role r: user.getRoles()){
            GrantedAuthority role = new SimpleGrantedAuthority(r.name());

            assertTrue(result.getAuthorities().contains(role));


        }
    }


}