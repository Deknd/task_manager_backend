package com.knd.developer.task_manager.service.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Выдает данные, для настройки бина редис, данные хранятся в application.yml
 */
@Component
@Data
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {
    private String host;
    private Integer port;
    private String password;
}
