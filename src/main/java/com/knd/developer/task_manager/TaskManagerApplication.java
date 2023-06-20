package com.knd.developer.task_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties
@EnableCaching
public class TaskManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
    }

}
