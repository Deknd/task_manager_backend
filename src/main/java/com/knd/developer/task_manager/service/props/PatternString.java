package com.knd.developer.task_manager.service.props;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Паттерны для проверки валидности вводимых данных
 */
@Component
@Data
public class PatternString {
    private final Pattern FORBIDDEN_JS_CHARS_PATTERN = Pattern.compile("[<>&\']");
    private final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

}
