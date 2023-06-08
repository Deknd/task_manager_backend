package com.knd.developer.task_manager.initializer;

import com.redis.testcontainers.RedisContainer;
import lombok.experimental.UtilityClass;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.utility.DockerImageName;

@UtilityClass
public class Redis {

    private final DockerImageName imageName = DockerImageName.parse("redis:7.2-rc-alpine");

    public static final RedisContainer container = new RedisContainer(imageName)
            .withExposedPorts(6444)
            .withEnv("REDIS_PASSWORD", "c2ZzZGZld2Zkc3ZzZGZzZWZz")
            .withCommand("redis-server", "--requirepass", "c2ZzZGZld2Zkc3ZzZGZzZWZz");


//, "--save", "20", "1", "--loglevel", "warning"
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.data.redis.host=" + container.getHost(),
                    "spring.data.redis.password=c2ZzZGZld2Zkc3ZzZGZzZWZz",
                    "spring.data.redis.port="+container.getFirstMappedPort()
            ).applyTo(applicationContext);
        }
    }
}
