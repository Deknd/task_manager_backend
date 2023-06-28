package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.exception.AccessDeniedException;
import com.knd.developer.task_manager.domain.tokens.Tokens;
import com.knd.developer.task_manager.repository.TokensRepository;
import com.knd.developer.task_manager.service.TokensService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokensServiceImpl implements TokensService {

    private final TokensRepository tokenRepository;

    /**
     * Проверяет данные, если все необходимые поля(id, refreshToken, secretKey) заполнены, сохраняет токен в БД.
     * Если нет одного или более обязательного поля(id, refreshToken, secretKey), то выкидывает исключение AccessDeniedException.
     *
     * @param token - Tokens объект, для хранения токенов в памяти, поля: id, refreshToken, secretKey - не могут быть null
     * @return возвращает обратно token
     */
    public Tokens addToken(Tokens token) {

        if (token.getId() == null || token.getRefreshToken() == null || token.getSecretKey() == null)
            throw new AccessDeniedException();
        return tokenRepository.save(token);

    }

    /**
     * Достает из БД все токены(Tokens) и сравнивает их с полученным token(access или refresh), при совпадении возвращает токен(Tokens).
     * Если совпадений нет, вернет null.
     *
     * @param token - токен полученный от клиента
     * @return - возвращает Tokens принадлежащий пользователю
     */
    public Tokens getTokenForByToken(String token) {
        Iterable<Tokens> tokens = tokenRepository.findAll();
        for (Tokens result : tokens) {
            if (result.getRefreshToken().equals(token)) {
                return result;
            }

            if (result.getAccessToken().equals(token)) {
                return result;
            }


        }
        return null;
    }

    /**
     * Делает запрос в БД. Если токен существует, вернет Optional с данным token, иначе вернет пустой Optional
     *
     * @param id  id(String) пользователя
     * @return  Если токен существует, вернет Optional с данным token, иначе вернет пустой Optional
     */
    public Optional<Tokens> getTokenById(String id) {
        return tokenRepository.findById(id);
    }

    /**
     * Делает запрос в БД на удаление по указанному id токена
     *
     * @param id  id(String) пользователя
     */
    public void deleteToken(String id) {
        tokenRepository.deleteById(id);


    }

}
