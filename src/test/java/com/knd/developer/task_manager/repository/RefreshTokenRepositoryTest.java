package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.domain.refresh.RefreshToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@DataRedisTest
@SpringJUnitConfig
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    String id = UUID.randomUUID().toString();


    @Test
    void refreshTokenRepository_SaveRefreshToken_ReturnRefreshToken() {

        //given
        String token = "RefreshToken";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(id);
        refreshToken.setRefreshToken(token);


        //when

        RefreshToken resultToken = refreshTokenRepository.save(refreshToken);

        //then
        assertNotNull(resultToken);
        assertEquals(refreshToken.getId(), resultToken.getId());
        assertEquals(refreshToken.getRefreshToken(), resultToken.getRefreshToken());

        deleteCache(resultToken);

    }

    @Test
    void refreshTokenRepository_SaveAndUpdateRefreshToken_ReturnRefreshToken() {

        //given
        String token = "RefreshToken";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(id);
        refreshToken.setRefreshToken(token);
        String tokenUpdate = "UpdateRefreshToken";
        RefreshToken refreshTokenUpdate = new RefreshToken();
        refreshTokenUpdate.setId(id);
        refreshTokenUpdate.setRefreshToken(tokenUpdate);

        //when
        refreshTokenRepository.save(refreshToken);

        RefreshToken resultToken = refreshTokenRepository.save(refreshTokenUpdate);


        //then
        assertNotNull(resultToken);
        assertEquals(refreshToken.getId(), resultToken.getId());
        assertNotEquals(refreshToken.getRefreshToken(),resultToken.getRefreshToken());


        assertEquals(refreshTokenUpdate.getRefreshToken(), resultToken.getRefreshToken());

        deleteCache(resultToken);

    }

    @Test
    void refreshTokenRepository_FindById_ReturnRefreshToken() {

        String token = "RefreshToken";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(id);
        refreshToken.setRefreshToken(token);
        refreshTokenRepository.save(refreshToken);

        RefreshToken result = refreshTokenRepository.findById(id).orElse(null);

        assertNotNull(result);
        assertEquals(refreshToken.getId(), result.getId());
        assertEquals(refreshToken.getRefreshToken(), result.getRefreshToken());

        deleteCache(result);


    }


    @Test
    void refreshTokenRepository_FindById_ReturnNull() {


        RefreshToken result = refreshTokenRepository.findById(id).orElse(null);

        assertNull(result);


    }

    @Test
    void refreshTokenRepository_DeleteRefreshToken() {

        String token = "RefreshToken";
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(id);
        refreshToken.setRefreshToken(token);
        refreshTokenRepository.save(refreshToken);

        refreshTokenRepository.delete(refreshToken);

        RefreshToken result = refreshTokenRepository.findById(id).orElse(null);

        assertNull(result);


    }

    void deleteCache(RefreshToken refreshToken) {
        refreshTokenRepository.delete(refreshToken);

    }


}