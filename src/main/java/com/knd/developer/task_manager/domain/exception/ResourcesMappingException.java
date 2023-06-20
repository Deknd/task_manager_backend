package com.knd.developer.task_manager.domain.exception;

import lombok.Data;

/**
 * Ошибка возникает, когда пользователь пытается зайти вместо, куда доступ ему закрыт
 */
@Data
public class ResourcesMappingException extends RuntimeException{
    public ResourcesMappingException(String message) {
        this.message = message;
    }
    private String message;
}
