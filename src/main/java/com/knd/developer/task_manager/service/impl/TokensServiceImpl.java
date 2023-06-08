package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.exception.AccessDeniedException;
import com.knd.developer.task_manager.domain.tokens.Tokens;
import com.knd.developer.task_manager.repository.TokensRepository;
import com.knd.developer.task_manager.service.TokensService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokensServiceImpl implements TokensService {

    private final TokensRepository tokenRepository;

    /**
     * Сохраняет объект Tokens в редис
     * @param token - Tokens объект, для хранения токенов в памяти, поля: id, refreshToken, secretKey - не могут быть null
     * @return возврощает обратно token
     */
    public Tokens addToken(Tokens token){

            if(token.getId()==null  || token.getRefreshToken()==null || token.getSecretKey() == null)
                throw new AccessDeniedException();
            return tokenRepository.save(token);

    }

    /**
     * Ищет в памяти объект Tokens по текстовому токену
     * @param token - токен полученый от клиента
     * @return - возврощает Tokens принадлежащий пользователю
     */
    public Tokens getTokenForByToken(String token){
       Iterable<Tokens> tokens=tokenRepository.findAll();
       for(Tokens result: tokens) {
           if (result.getRefreshToken().equals(token) ) {
               return result;
           }

           if(result.getAccessToken().equals(token)){

               return result;
           }


       }
       return null;
    }

    /**
     * Ищет в памяти Token по ID пользователя
     * @param id - id пользователя
     * @return - возвращает Token принадлежащий данному пользователю
     */
    public Optional<Tokens> getTokenById(String id){
        return tokenRepository.findById(id);
    }

    /**
     * Должен удалять токен пользователя из репозитория
     * @param id - id пользователя
     */
    public void deleteToken(String id){
        tokenRepository.deleteById(id);


    }

}
