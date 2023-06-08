package com.knd.developer.task_manager.service;

import com.knd.developer.task_manager.domain.tokens.Tokens;

import java.util.Optional;

public interface TokensService {
    /**
     * Сохраняет объект Tokens в редис
     * @param token - Tokens объект, для хранения токенов в памяти, поля: id, refreshToken, secretKey - не могут быть null
     * @return возврощает обратно token
     */
    Tokens addToken(Tokens token);
    /**
     * Ищет в памяти Token по ID пользователя
     * @param id - id пользователя
     * @return - возвращает Token принадлежащий данному пользователю
     */
    Optional<Tokens> getTokenById(String id);
    /**
     * Должен удалять токен пользователя из репозитория
     * @param id - id пользователя
     */
    void deleteToken(String id);

    /**
     * Ищет в памяти объект Tokens по текстовому токену
     * @param token - токен полученый от клиента
     * @return - возврощает Tokens принадлежащий пользователю
     */
    Tokens getTokenForByToken(String refresh);

}
