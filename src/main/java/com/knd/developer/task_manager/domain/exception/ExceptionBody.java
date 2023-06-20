package com.knd.developer.task_manager.domain.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionBody {

    private String message;
    private Map<String,String> errors;

    public ExceptionBody(String message){
        this.message=message;

    }
}
