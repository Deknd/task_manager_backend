package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.exception.AccessDeniedException;
import com.knd.developer.task_manager.domain.refresh.RefreshToken;
import com.knd.developer.task_manager.repository.RefreshTokenRepository;
import com.knd.developer.task_manager.service.RefreshTokenService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final  RefreshTokenRepository tokenRepository;

    /**
     * Сохраняет объект RefreshToken в редис
     * @param token - данный объект должен быть полным(все поля должны быть заполнены)
     *              иначе выдаст ошибку AccessDeniedException
     * @return возврощает обратно token
     */
    public RefreshToken addToken(RefreshToken token){
        try{
            if(token.getId()==null  || token.getRefreshToken()==null || token.getSecretKey() == null)
                throw new AccessDeniedException();
            return tokenRepository.save(token);
        }catch (NullPointerException e){
            throw new AccessDeniedException();
        }
    }

    /**
     * Ищет в памяти объект RefreshToken по текстовому рефреш токену
     * @param refresh - токен полученый от клиента
     * @return - возврощает RefreshToken принадлежащий пользователю
     */
    public RefreshToken getTokenForByToken(String refresh){
       Iterable<RefreshToken> tokens=tokenRepository.findAll();
       for(RefreshToken result: tokens){
           if (result.getRefreshToken().equals(refresh)) {
               return result;
           }
        }
       throw new AccessDeniedException();
    }

    /**
     * Ищет в памяти RefreshToken по ID пользователя
     * @param id - id пользователя
     * @return - возвращает RefreshToken принадлежащий данному пользователю
     */
    public Optional<RefreshToken> getTokenById(String id){
        return tokenRepository.findById(id);
    }

    /**
     * Должен удалять Рефреш токен пользователя из репозитория
     * @param id - id пользователя
     */
    public void deleteToken(String id){
        tokenRepository.deleteById(id);


    }

}
