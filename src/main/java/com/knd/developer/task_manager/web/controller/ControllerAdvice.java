package com.knd.developer.task_manager.web.controller;

import com.knd.developer.task_manager.domain.exception.AccessDeniedException;
import com.knd.developer.task_manager.domain.exception.ExceptionBody;
import com.knd.developer.task_manager.domain.exception.ResourceNotFoundException;
import com.knd.developer.task_manager.domain.exception.ResourcesMappingException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handlerResourceNotFoundException(ResourceNotFoundException e){
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(ResourcesMappingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handlerResourcesMappingException(ResourcesMappingException e){
        return new ExceptionBody(e.getMessage());
    }
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerIllegalStateException(IllegalStateException e){
        return new ExceptionBody(e.getMessage());
    }
    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handlerAccessDeniedException(AccessDeniedException e){
        return new ExceptionBody("Access, denied"+e.getMessage());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){
        ExceptionBody exceptionBody=new ExceptionBody("Validation failed."+e.getMessage());
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        exceptionBody.setErrors(errors.stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
        return exceptionBody;
    }
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerConstraintViolationException(ConstraintViolationException e){
        ExceptionBody exceptionBody= new ExceptionBody("Validation failed."+e.getMessage());
        exceptionBody.setErrors(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation ->violation.getPropertyPath().toString(),
                        violation ->violation.getMessage()
                )));
        return exceptionBody;
    }
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handlerException(Exception e){
        return new ExceptionBody("Internal error."+ e.getMessage());
    }
}
