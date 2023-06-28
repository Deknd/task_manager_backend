package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.domain.tokens.Tokens;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


/**
 * храниться и возврощает токены в(из)  редис
 */
public interface TokensRepository extends CrudRepository<Tokens, String> {

    /**
     * Сохраняет Tokens в БД.
     *
     * @param tokens (Tokens) должен быть заполнены: id, refreshToken, secretKey.
     * @return возвращает Tokens, который был получен
     */
    @Override
    Tokens save(Tokens tokens);

    /**
     * Достает Tokens из БД по id
     *
     * @param id (String) id пользователя
     * @return возвращает Optional с пользователем, если есть он в БД, иначе пустой Optional
     */
    @Override
    Optional<Tokens> findById(String id);

    /**
     * Удаляет Tokens по
     *
     * @param tokens
     */
    @Override
    void delete(Tokens tokens);

    @Override
    Iterable<Tokens> findAll();


}
