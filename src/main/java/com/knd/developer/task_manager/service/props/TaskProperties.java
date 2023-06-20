package com.knd.developer.task_manager.service.props;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data

//@ConfigurationProperties(prefix = "entity.task")
public class TaskProperties {
    @Value("${entity.task.max_length_title}")
    private Long max_length_title;
    @Value("${entity.task.max_length_description}")
    private  Long max_length_description;
}
