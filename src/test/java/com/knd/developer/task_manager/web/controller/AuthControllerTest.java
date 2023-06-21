package com.knd.developer.task_manager.web.controller;

import com.knd.developer.task_manager.IntegrationTestBase;
import com.knd.developer.task_manager.config.ApplicationConfig;
import com.knd.developer.task_manager.service.props.JwtProperties;
import com.knd.developer.task_manager.web.dto.auth.LoginRequest;
import com.knd.developer.task_manager.web.dto.auth.RefreshRequest;
import com.knd.developer.task_manager.web.dto.user.request.UserCreateRequestDto;
import com.knd.developer.task_manager.web.dto.user.response.UserAndTokenResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@Import(ApplicationConfig.class)

class AuthControllerTest extends IntegrationTestBase {


    @Autowired
    private JwtProperties jwtProperties;
    private final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'");


    @Test
    public void authController_LoginWhenEmptyData_ShouldReturnStatusIsUnauthorized() throws Exception {
        LoginRequest loginRequest = new LoginRequest("", "");
        String log = objectMapper.writeValueAsString(loginRequest);


        MvcResult result = this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(log))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();
        String expectedErrorMessage = "{\"message\":\"Authentication failed\",\"errors\":null}";

        assertEquals(expectedErrorMessage, responseBody);

    }

    @Test
    public void authController_LoginWhenFalseData_ShouldReturnStatusIsUnauthorized() throws Exception {
        String log1 = objectMapper.writeValueAsString(new LoginRequest("this", "yrDe"));
        String log2 = objectMapper.writeValueAsString(new LoginRequest("this@mail.ru", "yrDe"));
        String log3 = objectMapper.writeValueAsString(new LoginRequest("johndoe@mail.com", "yrDe"));
        String log4 = objectMapper.writeValueAsString(new LoginRequest("this", "122345"));
        String log5 = objectMapper.writeValueAsString(new LoginRequest("johndoe@mail.com", "yrDe"));
        String[] logs = {log1, log2, log3, log4, log5};


        for (String log : logs) {
            MvcResult result = this.mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(log))
                    .andDo(print())
                    .andExpect(status().isUnauthorized())
                    .andReturn();
            String responseBody = result.getResponse().getContentAsString();
            String expectedErrorMessage = "{\"message\":\"Authentication failed\",\"errors\":null}";
            assertEquals(expectedErrorMessage, responseBody);

        }
    }

    @Test
    public void authController_LoginWhenTrueData_ShouldReturnUserAndOkStatus() throws Exception {
        LoginRequest loginRequest = new LoginRequest("johndoe@mail.com", "12345");
        String log = objectMapper.writeValueAsString(loginRequest);


        MvcResult result = this.mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(log))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
        String responseBody = result.getResponse().getContentAsString();

        UserAndTokenResponseDto responseResult = objectMapper.readValue(responseBody, UserAndTokenResponseDto.class);
        assertNotNull(responseResult);
        assertEquals(1, responseResult.getId());
        assertEquals("John Doe", responseResult.getName());
        assertNotNull(responseResult.getAccessToken());
        assertNotNull(responseResult.getRefreshToken());
        LocalDateTime responseTimeExpiration = LocalDateTime.parse(responseResult.getExpiration(), DateTimeFormatter.ISO_DATE_TIME);
        assertTrue(responseTimeExpiration.isAfter(LocalDateTime.now(ZoneOffset.UTC)));
        LocalDateTime timeExpiration = LocalDateTime.now(ZoneOffset.UTC).plus(jwtProperties.getAccess(), ChronoUnit.MINUTES);
        assertTrue(responseTimeExpiration.isBefore(timeExpiration));
        assertFalse(responseResult.getTasks().isEmpty());
    }

    @Test
    void authController_RegisterWithNullDate_ResultValidationFailed() throws Exception {
        UserCreateRequestDto user1 = new UserCreateRequestDto();
        UserCreateRequestDto user2 = UserCreateRequestDto.builder()
                .name("Test")
                .password("test")
                .build();
        UserCreateRequestDto user3 = UserCreateRequestDto.builder()
                .name("test")
                .username("test@test.test")
                .build();
        UserCreateRequestDto user4 = UserCreateRequestDto.builder()
                .username("test@test.test")
                .password("test")
                .build();
        String[] userDtos = {
                objectMapper.writeValueAsString(user1),
                objectMapper.writeValueAsString(user2),
                objectMapper.writeValueAsString(user3),
                objectMapper.writeValueAsString(user4)
        };

        for (String dto : userDtos) {
            this.mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(dto))
                    .andDo(print())
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Test
    void authController_RegisterWithNoValidateDate_ResultBadRequest() throws Exception {
        UserCreateRequestDto userCreate1 = UserCreateRequestDto.builder()
                .name("/test>")
                .username("test<@test.ru")
                .password("</test$")
                .build();
        UserCreateRequestDto userCreate2 = UserCreateRequestDto.builder()
                .name("test")
                .username("test<@test.ru")
                .password("</test$")
                .build();
        UserCreateRequestDto userCreate3 = UserCreateRequestDto.builder()
                .name("test")
                .username("testtest.ru")
                .password("</test$")
                .build();

        String userString1 = objectMapper.writeValueAsString(userCreate1);
        String userString2 = objectMapper.writeValueAsString(userCreate2);
        String userString3 = objectMapper.writeValueAsString(userCreate3);


        this.mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userString1))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userString2))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userString3))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void authController_RegisterRepeatUser_ResultBadRequest() throws Exception {
        UserCreateRequestDto user = UserCreateRequestDto.builder()
                .name("Test")
                .username("johndoe@mail.com")
                .password("test")
                .build();
        String userDto = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDto))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void authController_RegisterRepeatUser_ResultOkStatus() throws Exception {

        UserCreateRequestDto user = UserCreateRequestDto.builder()
                .name("Test")
                .username(generateRandomWord(8) + "@test.com")
                .password("test")
                .build();
        String userDto = objectMapper.writeValueAsString(user);

        this.mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDto))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void authController_Refresh_ShouldReturnNewToken() throws Exception {
        UserAndTokenResponseDto user = getUser(1);

        RefreshRequest refresh = RefreshRequest.builder().refreshToken(user.getRefreshToken()).build();
        String refreshRequest = objectMapper.writeValueAsString(refresh);

        MvcResult mvcResult = this.mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshRequest))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        UserAndTokenResponseDto responseResult = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserAndTokenResponseDto.class);

        assertNotNull(responseResult);
        assertEquals(user.getId(), responseResult.getId());
        assertEquals(user.getName(), responseResult.getName());
        assertNotEquals(user.getAccessToken(),responseResult.getAccessToken());
        assertNotEquals(user.getRefreshToken(),responseResult.getRefreshToken());
        assertTrue(LocalDateTime.parse(user.getExpiration(),formatter).isBefore(LocalDateTime.parse(responseResult.getExpiration(),formatter)));
        assertEquals(user.getTasks().size(),responseResult.getTasks().size());

    }

    @Test
    void authController_RefreshFailToken_ShouldReturnStatusForbidden() throws Exception {
        UserAndTokenResponseDto user = getUser(1);

        RefreshRequest refresh = RefreshRequest.builder().refreshToken(user.getRefreshToken()+"dsgsfdfdsh").build();
        String refreshRequest = objectMapper.writeValueAsString(refresh);

        this.mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshRequest))
                .andDo(print())
                .andExpect(status().isForbidden());

    }
    @Test
    void authController_RefreshNull_ShouldReturnStatusForbidden() throws Exception {

        RefreshRequest refresh = RefreshRequest.builder().refreshToken("").build();
        String refreshRequest = objectMapper.writeValueAsString(refresh);

        this.mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(refreshRequest))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

    private String generateRandomWord(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }
        return sb.toString();
    }

}
