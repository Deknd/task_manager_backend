package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.domain.refresh.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
