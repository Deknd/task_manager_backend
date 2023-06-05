package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.exception.AccessDeniedException;
import com.knd.developer.task_manager.domain.refresh.RefreshToken;
import com.knd.developer.task_manager.repository.RefreshTokenRepository;
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
class RefreshTokenServiceImplTest {


    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    @Test
    void addToken_shouldSaveTokenRepository_ReturnToken() {
        RefreshToken token = new RefreshToken();
        token.setId(id_s());
        token.setRefreshToken("Refresh Token");
        token.setSecretKey(secretKey());
        when(refreshTokenRepository.save(token)).thenReturn(token);

        RefreshToken result = refreshTokenService.addToken(token);

        assertEquals(token, result);
        verify(refreshTokenRepository).save(token);

    }

    @Test
    void addToken_shouldTrySaveNotFullToken_ThrowExceptionAccessDeniedException() {
        RefreshToken token1 = new RefreshToken();
        token1.setId(null);
        token1.setRefreshToken("RefreshToken");
        token1.setSecretKey(secretKey());
        RefreshToken token2 = new RefreshToken();
        token2.setId(id_s());
        token2.setRefreshToken(null);
        token2.setSecretKey(secretKey());
        RefreshToken token3 = new RefreshToken();
        token3.setId(null);
        token3.setRefreshToken("RefreshToken");
        token3.setSecretKey(secretKey());


        assertThrows(AccessDeniedException.class, () -> refreshTokenService.addToken(token1));
        assertThrows(AccessDeniedException.class, () -> refreshTokenService.addToken(token2));
        assertThrows(AccessDeniedException.class, () -> refreshTokenService.addToken(token3));
        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void getTokenForByToken_ShouldReturnRefreshTokenIfTokenInMemory() {
        List<RefreshToken> tokens = new ArrayList<>();
        RefreshToken token1 = new RefreshToken();
        token1.setId(id_s());
        token1.setRefreshToken("RefreshToken 1");
        token1.setSecretKey(secretKey());
        RefreshToken token2 = new RefreshToken();
        token2.setId(id_s());
        token2.setRefreshToken("RefreshToken 2");
        token2.setSecretKey(secretKey());
        RefreshToken token3 = new RefreshToken();
        token3.setId(id_s());
        token3.setRefreshToken("RefreshToken 3");
        token3.setSecretKey(secretKey());
        tokens.add(token1);
        tokens.add(token2);
        tokens.add(token2);
        when(refreshTokenRepository.findAll()).thenReturn(tokens);

        RefreshToken result = refreshTokenService.getTokenForByToken(token2.getRefreshToken());

        assertEquals(token2.getRefreshToken(), result.getRefreshToken());
        verify(refreshTokenRepository).findAll();

    }

    @Test
    void getTokenForByToken_ShouldThrowExceptionAsNoGivenToken() {
        List<RefreshToken> tokens = new ArrayList<>();
        RefreshToken token1 = new RefreshToken();
        token1.setId(id_s());
        token1.setRefreshToken("RefreshToken 1");
        token1.setSecretKey(secretKey());
        RefreshToken token2 = new RefreshToken();
        token2.setId(id_s());
        token2.setRefreshToken("RefreshToken 2");
        token2.setSecretKey(secretKey());
        RefreshToken token3 = new RefreshToken();
        token3.setId(id_s());
        token3.setRefreshToken("RefreshToken 3");
        token3.setSecretKey(secretKey());
        tokens.add(token1);
        tokens.add(token2);
        tokens.add(token2);
        when(refreshTokenRepository.findAll()).thenReturn(tokens);

        assertThrows(AccessDeniedException.class, () -> refreshTokenService.getTokenForByToken("6O8rCX8"));
        verify(refreshTokenRepository).findAll();
    }

    @Test
    void getTokenById_ShouldReturnRefreshTokenIfTokenThereIsOrReturnOptionalOfNull() {
        RefreshToken token = new RefreshToken();
        token.setId(id_s());
        token.setRefreshToken("Refresh Token");
        token.setSecretKey(secretKey());
        when(refreshTokenRepository.findById(Mockito.eq(token.getId()))).thenReturn(Optional.of(token));

        Optional<RefreshToken> result = refreshTokenService.getTokenById(token.getId());
        Optional<RefreshToken> isNull = refreshTokenService.getTokenById(id_s());

        assertTrue(result.isPresent());
        assertEquals(token, result.get());
        assertTrue(isNull.isEmpty());
        verify(refreshTokenRepository, times(2)).findById(any(String.class));
    }

    @Test
    void deleteToken_ShouldDeleteTokenInDataBase() {
        String id = id_s();
        refreshTokenService.deleteToken(id);

        verify(refreshTokenRepository).deleteById(id);
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