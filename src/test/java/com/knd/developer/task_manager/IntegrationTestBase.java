package com.knd.developer.task_manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.knd.developer.task_manager.initializer.Postgres;
import com.knd.developer.task_manager.web.dto.auth.LoginRequest;
import com.knd.developer.task_manager.web.dto.user.response.UserAndTokenResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.utility.TestcontainersConfiguration;

import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT


)

@ActiveProfiles("test")
@ContextConfiguration(initializers = {
        Postgres.Initializer.class,
})
@AutoConfigureMockMvc

public abstract class IntegrationTestBase {
    static {
        // Отключаем проверку окружения Testcontainers
        TestcontainersConfiguration.getInstance().updateUserConfig("testcontainers.environment.checks", "false");//updateGlobalConfig("testcontainers.environment.checks", "false");
    }

    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    protected WebTestClient webTestClient; // available with Spring WebFlux
    protected ObjectMapper objectMapper;

    @BeforeAll
    static void init() {
        Postgres.container.start();

    }

    @BeforeEach
    public void setting() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        objectMapper = new ObjectMapper();
    }
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    protected static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
    protected UserAndTokenResponseDto getUser(int number) throws Exception {
        LoginRequest loginRequest;
        switch (number) {
            case 2:
                loginRequest = new LoginRequest("mikesmith@yahoo.com", "12345");
                break;
            case 3:
                loginRequest = new LoginRequest("PcdN1UpsE7@mail.ru", "12345");
                break;
            case 4:
                loginRequest= new LoginRequest("madMvkCR1Yh@yahoo.com", "12345");
                break;
            case 5:
                loginRequest= new LoginRequest("m57NMah@yahoo.com", "12345");
                break;
            case 6:
                loginRequest= new LoginRequest("BlaanOdessa@yahoo.com", "12345");
                break;
            case 7:
                loginRequest= new LoginRequest("bud_louis@yahoo.com","12345");
                break;
            case 8:
                loginRequest= new LoginRequest("janez_clifton@yahoo.com","12345");
                break;
            case 9:
                loginRequest= new LoginRequest("albana_orfeo@yahoo.com","12345");
                break;
            case 10:
                loginRequest= new LoginRequest("yıldırım_adela@yahoo.com","12345");
                break;
            default:
                loginRequest = new LoginRequest("johndoe@mail.com", "12345");
        }
        String log = objectMapper.writeValueAsString(loginRequest);

        MvcResult result = this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(log))
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();

        return objectMapper.readValue(responseBody, UserAndTokenResponseDto.class);

    }
}

