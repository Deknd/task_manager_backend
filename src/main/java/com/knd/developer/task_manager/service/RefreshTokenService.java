package com.knd.developer.task_manager.service;

import com.knd.developer.task_manager.domain.refresh.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    /**
     * Сохраняет объект RefreshToken в редис
     * @param token - данный объект должен быть полным(все поля должны быть заполнены)
     *              иначе выдаст ошибку AccessDeniedException
     * @return возврощает обратно token
     */
    RefreshToken addToken(RefreshToken token);
    /**
     * Ищет в памяти RefreshToken по ID пользователя
     * @param id - id пользователя
     * @return - возвращает RefreshToken принадлежащий данному пользователю
     */
    Optional<RefreshToken> getTokenById(String id);
    /**
     * Должен удалять Рефреш токен пользователя из репозитория
     * @param id - id пользователя
     */
    void deleteToken(String id);
    /**
     * Ищет в памяти объект RefreshToken по текстовому рефреш токену
     * @param refresh - токен полученый от клиента
     * @return - возврощает RefreshToken принадлежащий пользователю
     */
    RefreshToken getTokenForByToken(String refresh);

}
