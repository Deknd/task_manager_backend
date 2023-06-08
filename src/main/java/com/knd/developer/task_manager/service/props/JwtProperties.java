package com.knd.developer.task_manager.service.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Используется для отображения данных о времени действие токенов, данные хранятся в application.yml
 */
@Component
@Data
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    private Long access;
    private Long refresh;

}
