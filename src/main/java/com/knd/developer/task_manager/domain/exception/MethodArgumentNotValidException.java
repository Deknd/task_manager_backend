package com.knd.developer.task_manager.domain.exception;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MethodArgumentNotValidException extends RuntimeException{
    public MethodArgumentNotValidException(String nameField, String message){
        errors = new HashMap<>();
        errors.put(nameField,message);
    }

    private Map<String, String> errors;
}
