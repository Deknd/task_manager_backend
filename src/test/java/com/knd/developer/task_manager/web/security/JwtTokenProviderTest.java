package com.knd.developer.task_manager.web.security;

import com.knd.developer.task_manager.domain.exception.AccessDeniedException;
import com.knd.developer.task_manager.domain.tokens.Tokens;
import com.knd.developer.task_manager.domain.user.Role;
import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.service.TokensService;
import com.knd.developer.task_manager.service.UserService;
import com.knd.developer.task_manager.service.props.JwtProperties;
import com.knd.developer.task_manager.web.dto.user.response.UserAndTokenResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider tokenProvider;
    @Mock
    private JwtProperties jwtProperties;
   @Mock
   private JwtEntityFactory entityFactory;
    @Mock
    private UserService userService;
    @Mock
    private TokensService tokensService;


    @Test
    void createAccessToken_ShouldCreateAccessToken() {

        Long userId = 412L;
        String username = "study@mail.com";
        Set<Role> roles = Set.of(Role.ROLE_USER);
        Instant validity = Instant.now().plus(10, ChronoUnit.MINUTES);
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        Tokens tokens = new Tokens();
        tokens.setSecretKey(keyGenerator.generateKey());

        when(tokensService.getTokenById(userId.toString())).thenReturn(Optional.of(tokens));
        when(tokensService.addToken(any(Tokens.class))).thenAnswer(invocationOnMock -> {
            Tokens result = invocationOnMock.getArgument(0);
            return result;
        });


        String token = tokenProvider.createAccessToken(userId, username, roles, validity);
        assertNotNull(token);
    }

    @Test
    void createAccessToken_ShouldTrowsException() {

        Long userId = 561L;
        String username = "some";
        Set<Role> roles = new HashSet<>();
        Instant validity = Instant.now();


        when(tokensService.getTokenById(userId.toString())).thenReturn(Optional.empty());


        assertThrows(AccessDeniedException.class, () -> tokenProvider.createAccessToken(userId, username, roles, validity));


    }

    @Test
    void createRefreshToken_ShouldCreateRefreshToken() {
        Long userId = 412L;
        String username = "study@mail.com";

        when(jwtProperties.getRefresh()).thenReturn(1L);
        when(tokensService.addToken(any(Tokens.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String refresh = tokenProvider.createRefreshToken(userId, username);

        assertNotNull(refresh);
        ArgumentCaptor<Tokens> requestCaptor = ArgumentCaptor.forClass(Tokens.class);
        verify(tokensService).addToken(requestCaptor.capture());
        Tokens tokens = requestCaptor.getValue();
        assertEquals(userId.toString(), tokens.getId());
        assertNotNull(tokens.getSecretKey());
        assertNotNull(tokens.getRefreshToken());

    }

    @Test
    void refreshUserToken_ShouldCreateUserAndTokenResponse() {
        Long userId = 271L;
        String username = "queen";
        Tokens tokens = generationTokens(userId, username);
        User user = mock(User.class);
        Instant validity = Instant.now()
                .plus(15, ChronoUnit.MINUTES);
        when(tokensService.getTokenForByToken(any(String.class))).thenReturn(tokens);
        when(userService.getById(eq(userId))).thenReturn(user);

        when(tokensService.addToken(any(Tokens.class))).thenReturn(tokens);
        when(tokensService.getTokenById(any())).thenReturn(Optional.of(tokens));


        UserAndTokenResponseDto userResponse = tokenProvider.refreshUserToken(tokens.getRefreshToken(), validity);
        assertEquals(tokens.getAccessToken(), userResponse.getAccessToken());
        assertEquals(tokens.getRefreshToken(), userResponse.getRefreshToken());
        assertEquals(validity.toString(), userResponse.getExpiration());

    }

    @Test
    void refreshUserToken_ShouldTrowsException() {
        Long userId = 271L;
        String username = "queen";
        Tokens tokens = generationTokens(userId, username);
        Instant validity = Instant.now()
                .plus(15, ChronoUnit.MINUTES);
        when(tokensService.getTokenForByToken(any(String.class))).thenReturn(tokens);


        assertThrows(AccessDeniedException.class, () -> tokenProvider.refreshUserToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcmhpcG92YUBnbWFpbC5jb20iLCJpZCI6NTEwLCJleHAiOjE2ODY2NTE2NzR9.eqCuk4FTJBGnDH65g608C0CrcUD1EmgZ4AYq3NThKCI", validity));
        assertThrows(AccessDeniedException.class, () -> tokenProvider.refreshUserToken("discuss", validity));
    }

    @Test
    void validateToken_ShouldValidateToken() {
        Tokens tokens = generationTokens(782L, "poem");
        when(tokensService.getTokenForByToken(any(String.class))).thenReturn(tokens);

        assertTrue(tokenProvider.validateToken(tokens.getAccessToken()));
        assertTrue(tokenProvider.validateToken(tokens.getRefreshToken()));
        assertFalse(tokenProvider.validateToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhcmhpcG92YUBnbWFpbC5jb20iLCJpZCI6NTEwLCJleHAiOjE2ODY2NTE2NzR9.eqCuk4FTJBGnDH65g608C0CrcUD1EmgZ4AYq3NThKCI"));
        assertFalse(tokenProvider.validateToken("u6jcCRN9"));

    }

    @Test
    void getAuthentication_ShouldReturnAuthentication(){
        Long id = 741L;
        String username = "fs8L1ToF";
        Tokens tokens = generationTokens(id,username);
        User user = mock(User.class);
        UserDetails userDetails = mock(JwtEntity.class);
        when(tokensService.getTokenForByToken(any(String.class))).thenReturn(tokens);
        when(userService.getById(eq(id))).thenReturn(user);
        when(entityFactory.create(eq(user))).thenReturn((JwtEntity) userDetails);

        Authentication authentication = tokenProvider.getAuthentication(tokens.getAccessToken());
        Authentication authentication2 = tokenProvider.getAuthentication(tokens.getRefreshToken());

        assertNotNull(authentication);
        assertNull(authentication2);

    }
    @Test
    void logoutUser_ShouldDeleteTokensForMemory(){
        Tokens tokens = generationTokens(789L, "Obcu5jbG");
        when(tokensService.getTokenForByToken(tokens.getRefreshToken())).thenReturn(tokens);

        tokenProvider.logoutUser(tokens.getRefreshToken());

        verify(tokensService).deleteToken(eq(tokens.getId()));
    }

    private Tokens generationTokens(Long userId, String username) {
        KeyGenerator keyGenerator;
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        SecretKey secretKey = keyGenerator.generateKey();

        Claims claims = Jwts.claims().setSubject(username);
        claims.put("id", userId);

        Instant validity = Instant.now()
                .plus(24, ChronoUnit.HOURS);

        Tokens token = new Tokens();
        token.setId(userId.toString());
        token.setSecretKey(secretKey);
        token.setRefreshToken(Jwts.builder()
                .setClaims(claims)
                .setExpiration(Date.from(validity))
                .signWith(token.getSecretKey())
                .compact());
        Claims claims2 = Jwts.claims().setSubject(username);
        claims2.put("id", userId);
        claims2.put("roles", Set.of(Role.ROLE_USER));
        token.setAccessToken(Jwts.builder()
                .setClaims(claims2)
                .setExpiration(Date.from(validity))
                .signWith(token.getSecretKey())
                .compact());
        return token;
    }


}