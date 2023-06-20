package com.knd.developer.task_manager.service.props;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Паттерны для проверки валидности вводимых данных
 */
@Component
@Data
public class PatternString {


    @Value("${entity.pattern.forbidden_js_chars_pattern}")
    private String forbidden_js_chars_pattern;
    @Value("${entity.pattern.email_pattern}")
    private String email_pattern;
    private Pattern EMAIL_PATTERN;
    private Pattern FORBIDDEN_JS_CHARS_PATTERN;

    public Pattern getFORBIDDEN_JS_CHARS_PATTERN() {
        if (FORBIDDEN_JS_CHARS_PATTERN == null) {
            FORBIDDEN_JS_CHARS_PATTERN = Pattern.compile(forbidden_js_chars_pattern);
        }
        return FORBIDDEN_JS_CHARS_PATTERN;
    }

    public Pattern getEMAIL_PATTERN() {
        if (EMAIL_PATTERN == null) {
            EMAIL_PATTERN = Pattern.compile(email_pattern);
        }
        return EMAIL_PATTERN;
    }


}
