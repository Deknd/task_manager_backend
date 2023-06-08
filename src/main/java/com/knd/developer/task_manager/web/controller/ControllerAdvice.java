package com.knd.developer.task_manager.web.controller;

import com.knd.developer.task_manager.domain.exception.AccessDeniedException;
import com.knd.developer.task_manager.domain.exception.ExceptionBody;
import com.knd.developer.task_manager.domain.exception.ResourceNotFoundException;
import com.knd.developer.task_manager.domain.exception.ResourcesMappingException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.security.sasl.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionBody handlerResourceNotFoundException(ResourceNotFoundException e){
        e.printStackTrace();
        return new ExceptionBody(e.getMessage());
    }

    @ExceptionHandler(ResourcesMappingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handlerResourcesMappingException(ResourcesMappingException e){
        e.printStackTrace();

        return new ExceptionBody(e.getMessage());
    }
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerIllegalStateException(IllegalStateException e){

        return new ExceptionBody(e.getMessage());
    }
//    @ExceptionHandler({AccessDeniedException.class, org.springframework.security.access.AccessDeniedException.class})
//    @ResponseStatus(HttpStatus.FORBIDDEN)
//    public ExceptionBody handlerAccessDeniedException(org.springframework.security.access.AccessDeniedException e) {
//        ExceptionBody body = new ExceptionBody("Access, denied");
//
//        return body;
//    }
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handlerAccessDeniedException(AccessDeniedException e){
        ExceptionBody body = new ExceptionBody("Access, denied");

        return body;
    }
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionBody handlerAccessDeniedExceptionSpring(org.springframework.security.access.AccessDeniedException e){
        ExceptionBody body = new ExceptionBody("Access, denied");

        return body;
    }
    @ExceptionHandler(com.knd.developer.task_manager.domain.exception.MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerMethodArgumentNotValidException(com.knd.developer.task_manager.domain.exception.MethodArgumentNotValidException e){
        ExceptionBody exceptionBody=new ExceptionBody("Validation failed.");
        exceptionBody.setErrors(e.getErrors());

        return exceptionBody;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerMethodArgumentNotValidException(MethodArgumentNotValidException e){

        ExceptionBody exceptionBody=new ExceptionBody("Validation failed.");
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        exceptionBody.setErrors(errors.stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
        return exceptionBody;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerConstraintViolationException(ConstraintViolationException e){
        e.printStackTrace();

        ExceptionBody exceptionBody= new ExceptionBody("Validation failed.");
        exceptionBody.setErrors(e.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation ->violation.getPropertyPath().toString(),
                        violation ->violation.getMessage()
                )));
        return exceptionBody;
    }
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionBody handlerAuthenticationException(AuthenticationException e){
        e.printStackTrace();
        return new ExceptionBody("Authentication failed 1");
    }
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionBody handlerInternalAuthenticationServiceException(InternalAuthenticationServiceException e){
e.printStackTrace();
        return new ExceptionBody("Authentication failed 2");
    }
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionBody handlerBadCredentialsException(BadCredentialsException e){
        return new ExceptionBody("Authentication failed");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionBody handlerException(Exception e){
        e.printStackTrace();
        return new ExceptionBody("Internal error."+e.getMessage());
    }
}
