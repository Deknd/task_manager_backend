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

    @Test
    void addToken_shouldSaveTokenRepository_ReturnToken() {
        Tokens token = new Tokens();
        token.setId(id_s());
        token.setRefreshToken("Refresh Token");
        token.setSecretKey(secretKey());
        when(tokensRepository.save(token)).thenReturn(token);

        Tokens result = refreshTokenService.addToken(token);

        assertEquals(token, result);
        verify(tokensRepository).save(token);

    }

    @Test
    void addToken_shouldTrySaveNotFullToken_ThrowExceptionAccessDeniedException() {
        Tokens token1 = new Tokens();
        token1.setId(null);
        token1.setRefreshToken("RefreshToken");
        token1.setSecretKey(secretKey());
        Tokens token2 = new Tokens();
        token2.setId(id_s());
        token2.setRefreshToken(null);
        token2.setSecretKey(secretKey());
        Tokens token3 = new Tokens();
        token3.setId(null);
        token3.setRefreshToken("RefreshToken");
        token3.setSecretKey(secretKey());


        assertThrows(AccessDeniedException.class, () -> refreshTokenService.addToken(token1));
        assertThrows(AccessDeniedException.class, () -> refreshTokenService.addToken(token2));
        assertThrows(AccessDeniedException.class, () -> refreshTokenService.addToken(token3));
        verify(tokensRepository, never()).save(any(Tokens.class));
    }

    @Test
    void getTokenForByToken_ShouldReturnRefreshTokenIfTokenInMemory() {
        List<Tokens> tokens = new ArrayList<>();

        Tokens token1 = new Tokens();
        token1.setId(id_s());
        token1.setRefreshToken("RefreshToken 1");
        token1.setAccessToken("AccessToken 1");
        token1.setSecretKey(secretKey());

        Tokens token2 = new Tokens();
        token2.setId(id_s());
        token2.setAccessToken("AccessToken 2");
        token2.setRefreshToken("RefreshToken 2");
        token2.setSecretKey(secretKey());
        Tokens token3 = new Tokens();
        token3.setId(id_s());
        token3.setAccessToken("AccessToken 3");
        token3.setRefreshToken("RefreshToken 3");
        token3.setSecretKey(secretKey());

        tokens.add(token1);
        tokens.add(token2);
        tokens.add(token3);
        when(tokensRepository.findAll()).thenReturn(tokens);

        Tokens result = refreshTokenService.getTokenForByToken(token2.getRefreshToken());
        Tokens result2 = refreshTokenService.getTokenForByToken(token3.getAccessToken());

        assertEquals(token2.getRefreshToken(), result.getRefreshToken());
        assertEquals(token3, result2);
        verify(tokensRepository, times(2)).findAll();

    }

    @Test
    void getTokenForByToken_ShouldThrowExceptionAsNoGivenToken() {
        List<Tokens> tokens = new ArrayList<>();
        Tokens token1 = new Tokens();
        token1.setId(id_s());
        token1.setRefreshToken("RefreshToken 1");
        token1.setAccessToken("AccessToken 1");
        token1.setSecretKey(secretKey());

        Tokens token2 = new Tokens();
        token2.setId(id_s());
        token2.setAccessToken("AccessToken 2");
        token2.setRefreshToken("RefreshToken 2");
        token2.setSecretKey(secretKey());
        Tokens token3 = new Tokens();
        token3.setId(id_s());
        token3.setAccessToken("AccessToken 3");
        token3.setRefreshToken("RefreshToken 3");
        token3.setSecretKey(secretKey());
        tokens.add(token1);
        tokens.add(token2);
        tokens.add(token3);
        when(tokensRepository.findAll()).thenReturn(tokens);

        Tokens tokenForByToken = refreshTokenService.getTokenForByToken("6O8rCX8");
        assertNull(tokenForByToken);
        verify(tokensRepository).findAll();
    }

    @Test
    void getTokenById_ShouldReturnRefreshTokenIfTokenThereIsOrReturnOptionalOfNull() {
        Tokens token = new Tokens();
        token.setId(id_s());
        token.setRefreshToken("Refresh Token");
        token.setSecretKey(secretKey());
        when(tokensRepository.findById(Mockito.eq(token.getId()))).thenReturn(Optional.of(token));

        Optional<Tokens> result = refreshTokenService.getTokenById(token.getId());
        Optional<Tokens> isNull = refreshTokenService.getTokenById(id_s());

        assertTrue(result.isPresent());
        assertEquals(token, result.get());
        assertTrue(isNull.isEmpty());
        verify(tokensRepository, times(2)).findById(any(String.class));
    }

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

    private Long id_l() {
        UUID uuid = UUID.randomUUID();
        Long idLong = Math.abs(uuid.getMostSignificantBits());
        return idLong;
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