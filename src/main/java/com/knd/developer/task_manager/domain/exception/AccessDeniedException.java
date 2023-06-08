package com.knd.developer.task_manager.domain.exception;

/**
 * Ошибка возникает при проблемах с авторизации пользователя
 */
public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException() {
        super();
    }
}
