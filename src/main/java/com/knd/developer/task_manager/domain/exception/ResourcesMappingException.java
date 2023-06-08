package com.knd.developer.task_manager.domain.exception;

/**
 * Ошибка возникает, когда пользователь пытается зайти вместо, куда доступ ему закрыт
 */
public class ResourcesMappingException extends RuntimeException{
    public ResourcesMappingException(String message) {
        super(message);
    }
}
