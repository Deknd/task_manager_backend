package com.knd.developer.task_manager.domain.exception;

/**
 * Ошибка возникает, когда данные каким то образом не получилось найти
 */
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
