package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.refresh.RefreshToken;
import com.knd.developer.task_manager.repository.RefreshTokenRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class RefreshTokenService {

    private final  RefreshTokenRepository tokenRepository;


    public RefreshToken addToken(RefreshToken token){
        return tokenRepository.save(token);
    }

    public RefreshToken getTokenById(Long id){
        return tokenRepository.findById(id.toString()).orElse(null);
    }
    public void deleteToken(Long id){
        tokenRepository.delete(getTokenById(id));
    }

}
