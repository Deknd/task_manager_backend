package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.exception.AccessDeniedException;
import com.knd.developer.task_manager.domain.tokens.Tokens;
import com.knd.developer.task_manager.repository.TokensRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokensServiceImplTest {


    @Mock
    private TokensRepository tokensRepository;

    @InjectMocks
    private TokensServiceImpl refreshTokenService;

    //Проверяет данные, сохраняет токен в БД, возвращает тот же токен
    @Test
    void addToken_shouldSaveTokenRepository_ReturnToken() {
        Tokens token = Tokens.builder()
                .id(id_s())
                .refreshToken("Refresh token")
                .secretKey(secretKey()).build();

        when(tokensRepository.save(token)).thenReturn(token);

        Tokens result = refreshTokenService.addToken(token);

        assertEquals(token, result);
        verify(tokensRepository).save(token);

    }

    // Если нет одного или более обязательного поля(id, refreshToken, secretKey), то выкинет исключение AccessDeniedException
    @Test
    void addToken_shouldTrySaveNotFullToken_ThrowExceptionAccessDeniedException() {
        Tokens token1 = Tokens.builder()
                .refreshToken("Refresh token")
                .secretKey(secretKey())
                .build();

        Tokens token2 = Tokens.builder()
                .id(id_s())
                .secretKey(secretKey())
                .build();

        Tokens token3 = Tokens.builder()
                .id(id_s())
                .refreshToken("Refresh token")
                .build();



        assertThrows(AccessDeniedException.class, () -> refreshTokenService.addToken(token1));
        assertThrows(AccessDeniedException.class, () -> refreshTokenService.addToken(token2));
        assertThrows(AccessDeniedException.class, () -> refreshTokenService.addToken(token3));
        verify(tokensRepository, never()).save(any(Tokens.class));
    }

    //Достает из БД все токены(Tokens) и сравнивает их с полученным token(access или refresh), при совпадении возвращает токен(Tokens)
    @Test
    void getTokenForByToken_ShouldReturnRefreshTokenIfTokenInMemory() {

        Tokens token1 = Tokens.builder()
                .id(id_s())
                .refreshToken("RefreshToken 1")
                .accessToken("AccessToken 1")
                .secretKey(secretKey())
                .build();


        Tokens token2 = Tokens.builder()
                .id(id_s())
                .refreshToken("RefreshToken 2")
                .accessToken("AccessToken 2")
                .secretKey(secretKey())
                .build();

        Tokens token3 = Tokens.builder()
                .id(id_s())
                .refreshToken("RefreshToken 3")
                .accessToken("AccessToken 3")
                .secretKey(secretKey())
                .build();

        List<Tokens> tokens = List.of(token1, token2, token3);

        when(tokensRepository.findAll()).thenReturn(tokens);

        Tokens result = refreshTokenService.getTokenForByToken(token2.getRefreshToken());
        Tokens result2 = refreshTokenService.getTokenForByToken(token3.getAccessToken());

        assertEquals(token2, result);
        assertEquals(token3, result2);

    }

    //Если совпадений нет, вернет null
    @Test
    void getTokenForByToken_ShouldThrowExceptionAsNoGivenToken() {
        Tokens token1 = Tokens.builder()
                .id(id_s())
                .refreshToken("RefreshToken 1")
                .accessToken("AccessToken 1")
                .secretKey(secretKey())
                .build();


        Tokens token2 = Tokens.builder()
                .id(id_s())
                .refreshToken("RefreshToken 2")
                .accessToken("AccessToken 2")
                .secretKey(secretKey())
                .build();

        Tokens token3 = Tokens.builder()
                .id(id_s())
                .refreshToken("RefreshToken 3")
                .accessToken("AccessToken 3")
                .secretKey(secretKey())
                .build();

        List<Tokens> tokens = List.of(token1, token2, token3);

        when(tokensRepository.findAll()).thenReturn(tokens);

        Tokens tokenForByToken = refreshTokenService.getTokenForByToken("6O8rCX8");
        assertNull(tokenForByToken);
    }

    //Делает запрос в БД. Если токен существует, вернет Optional с данным токеном, иначе вернет пустой Optional
    @Test
    void getTokenById_ShouldReturnRefreshTokenIfTokenThereIsOrReturnOptionalOfNull() {
        Tokens token = Tokens.builder()
                .id(id_s())
                .refreshToken("RefreshToken")
                .accessToken("AccessToken")
                .secretKey(secretKey())
                .build();

        when(tokensRepository.findById(Mockito.eq(token.getId()))).thenReturn(Optional.of(token));

        Optional<Tokens> result = refreshTokenService.getTokenById(token.getId());
        Optional<Tokens> isNull = refreshTokenService.getTokenById(id_s());

        assertTrue(result.isPresent());
        assertEquals(token, result.get());
        assertTrue(isNull.isEmpty());
        verify(tokensRepository, times(2)).findById(any(String.class));
    }

    // Делает запрос в БД на удаление по указанному id токена
    @Test
    void deleteToken_ShouldDeleteTokenInDataBase() {
        String id = id_s();
        refreshTokenService.deleteToken(id);

        verify(tokensRepository).deleteById(id);
    }


    KeyGenerator keyGenerator;

    private String id_s() {
        UUID uuid = UUID.randomUUID();

        String id = uuid.toString();
        return id;
    }



    private SecretKey secretKey() {
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;

    }

}