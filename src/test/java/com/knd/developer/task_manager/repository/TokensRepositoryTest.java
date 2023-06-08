package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.IntegrationTestBase;
import com.knd.developer.task_manager.domain.tokens.Tokens;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


class TokensRepositoryTest extends IntegrationTestBase {

    @Autowired
    private TokensRepository tokensRepository;


    String id = UUID.randomUUID().toString();
    KeyGenerator keyGenerator;


    private SecretKey secretKey() {
        try {
            keyGenerator = KeyGenerator.getInstance("HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey;

    }


    @Test
    void saveRefreshToken_ReturnRefreshToken() {

        //given
        String token = "RefreshToken";
        Tokens tokens = new Tokens();
        tokens.setId(id);
        tokens.setRefreshToken(token);
        tokens.setSecretKey(secretKey());


        //when

        Tokens resultToken = tokensRepository.save(tokens);

        //then
        assertNotNull(resultToken);
        assertEquals(tokens.getId(), resultToken.getId());
        assertEquals(tokens.getRefreshToken(), resultToken.getRefreshToken());
        assertEquals(tokens.getSecretKey(), resultToken.getSecretKey());

        deleteCache(resultToken);

    }

    @Test
    void refreshTokenRepository_SaveAndUpdateRefreshToken_ReturnRefreshToken() {

        //given
        String token = "RefreshToken";
        Tokens tokens = new Tokens();
        tokens.setId(id);
        tokens.setRefreshToken(token);
        tokens.setSecretKey(secretKey());

        String tokenUpdate = "UpdateRefreshToken";
        Tokens tokensUpdate = new Tokens();
        tokensUpdate.setId(id);
        tokensUpdate.setRefreshToken(tokenUpdate);
        tokensUpdate.setSecretKey(secretKey());

        //when
        tokensRepository.save(tokens);

        Tokens resultToken = tokensRepository.save(tokensUpdate);


        //then
        assertNotNull(resultToken);
        assertEquals(tokens.getId(), resultToken.getId());
        assertNotEquals(tokens.getRefreshToken(), resultToken.getRefreshToken());
        assertNotEquals(tokens.getSecretKey(), resultToken.getSecretKey());


        assertEquals(tokensUpdate.getRefreshToken(), resultToken.getRefreshToken());
        assertEquals(tokensUpdate.getSecretKey(), resultToken.getSecretKey());

        deleteCache(resultToken);

    }

    @Test
    void refreshTokenRepository_FindById_ReturnRefreshToken() {

        String token = "RefreshToken";
        Tokens tokens = new Tokens();
        tokens.setId(id);
        tokens.setRefreshToken(token);
        tokens.setSecretKey(secretKey());
        tokensRepository.save(tokens);

        Tokens result = tokensRepository.findById(id).orElse(null);

        assertNotNull(result);
        assertEquals(tokens.getId(), result.getId());
        assertEquals(tokens.getRefreshToken(), result.getRefreshToken());
        assertEquals(tokens.getSecretKey(), result.getSecretKey());

        deleteCache(result);


    }


    @Test
    void refreshTokenRepository_FindById_ReturnNull() {


        Tokens result = tokensRepository.findById(id).orElse(null);

        assertNull(result);


    }

    @Test
    void refreshTokenRepository_DeleteRefreshToken() {

        String token = "RefreshToken";
        Tokens tokens = new Tokens();
        tokens.setId(id);
        tokens.setRefreshToken(token);
        tokens.setSecretKey(secretKey());
        tokensRepository.save(tokens);

        tokensRepository.delete(tokens);

        Tokens result = tokensRepository.findById(id).orElse(null);

        assertNull(result);


    }

    void deleteCache(Tokens tokens) {
        tokensRepository.delete(tokens);

    }


}