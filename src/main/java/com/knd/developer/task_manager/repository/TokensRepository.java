package com.knd.developer.task_manager.repository;

import com.knd.developer.task_manager.domain.tokens.Tokens;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


/**
 * храниться и возврощает токены в(из)  редис
 */
public interface TokensRepository extends CrudRepository<Tokens, String> {

    Tokens save(Tokens tokens);

    Optional<Tokens> findById(String id);

    void delete(Tokens tokens);

    Iterable<Tokens> findAll();



}
