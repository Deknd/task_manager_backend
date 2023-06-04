package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.domain.refresh.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


/**
 * храниться рефреш токен в памяти редис
 */
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    RefreshToken save(RefreshToken refreshToken);

    Optional<RefreshToken> findById(String id);

    void delete(RefreshToken refreshToken);


}
