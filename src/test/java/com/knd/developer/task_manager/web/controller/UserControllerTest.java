package com.knd.developer.task_manager.web.controller;

import com.knd.developer.task_manager.IntegrationTestBase;
import com.knd.developer.task_manager.domain.exception.ExceptionBody;
import com.knd.developer.task_manager.service.props.JwtProperties;
import com.knd.developer.task_manager.web.dto.auth.LoginRequest;
import com.knd.developer.task_manager.web.dto.user.request.UserUpdateRequestDto;
import com.knd.developer.task_manager.web.dto.user.response.UserAndTokenResponseDto;
import com.knd.developer.task_manager.web.dto.user.response.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureWebTestClient
class UserControllerTest extends IntegrationTestBase {

    @Autowired
    private JwtProperties jwtProperties;

    @Test
    void userController_Update_ShouldUpdateDateUserNamePasswordEmail() throws Exception {
        UserAndTokenResponseDto user = getUser(2);

        UserUpdateRequestDto userUpdate = UserUpdateRequestDto
                .builder()
                .id(user.getId())
                .oldPassword("12345")
                .newName("Test Test")
                .newUsername("testtest@test.test")
                .newPassword("test")
                .build();
        String userJson = objectMapper.writeValueAsString(userUpdate);

        FluxExchangeResult<String> result = webTestClient.put()
                .uri("/api/v1/users/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userJson)
                .exchange()


                .expectAll(
                        spec -> spec.expectStatus().isOk(),
                        spec -> spec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spec -> System.out.println(spec.returnResult(String.class))
                )
                .returnResult(String.class);

        assertNotNull(result);
        UserResponseDto userResult = objectMapper.readValue(result.getResponseBodyContent(), UserResponseDto.class );
        assertNotNull(userResult);
        assertEquals(user.getId(), userResult.getId());
        assertEquals(userUpdate.getNewName(), userResult.getName());
        assertNotNull(userResult.getListTask());

        LoginRequest loginRequest = new LoginRequest(userUpdate.getNewUsername(),userUpdate.getNewPassword());
        String log = objectMapper.writeValueAsString(loginRequest);

        FluxExchangeResult<String> returnResult = webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(log)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(String.class);
        assertNotNull(returnResult);
        UserAndTokenResponseDto responseDto = objectMapper.readValue(returnResult.getResponseBodyContent(),UserAndTokenResponseDto.class);
        assertNotNull(responseDto);
        assertEquals(userResult.getId(),responseDto.getId());
        assertEquals(userResult.getName(), responseDto.getName());
        assertEquals(userResult.getListTask(), responseDto.getTasks());


    }

    @Test
    void userController_Update_ShouldNoValidationNameDate() throws Exception {
        UserAndTokenResponseDto user = getUser(6);

        UserUpdateRequestDto userUpdate = UserUpdateRequestDto
                .builder()
                .id(user.getId())
                .oldPassword("12345")
                .newName("Test Te'>}]st")
                .newUsername("test9381@test.test")
                .newPassword("test")
                .build();
        String userJson = objectMapper.writeValueAsString(userUpdate);

        webTestClient.put()
                .uri("/api/v1/users/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userJson)
                .exchange()


                .expectAll(
                        spec -> spec.expectStatus().isBadRequest(),
                        spec -> spec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spec -> {
                            EntityExchangeResult<ExceptionBody> returnResult = spec.expectBody(ExceptionBody.class).returnResult();
                            assertNotNull(returnResult);
                            ExceptionBody exceptionBody = returnResult.getResponseBody();
                            assertNotNull(exceptionBody);
                            assertEquals("Validation failed.", exceptionBody.getMessage());

                        },
                        spec -> System.out.println(spec.returnResult(String.class))
                );

    }
    @Test
    void userController_Update_ShouldNoValidationEmeilDate() throws Exception {
        UserAndTokenResponseDto user = getUser(6);

        UserUpdateRequestDto userUpdate = UserUpdateRequestDto
                .builder()
                .id(user.getId())
                .oldPassword("12345")
                .newName("Test Test")
                .newUsername("test9381.>'}]@test.test")
                .newPassword("test")
                .build();
        String userJson = objectMapper.writeValueAsString(userUpdate);

        webTestClient.put()
                .uri("/api/v1/users/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userJson)
                .exchange()


                .expectAll(
                        spec -> spec.expectStatus().isBadRequest(),
                        spec -> spec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spec -> {
                            EntityExchangeResult<ExceptionBody> returnResult = spec.expectBody(ExceptionBody.class).returnResult();
                            assertNotNull(returnResult);
                            ExceptionBody exceptionBody = returnResult.getResponseBody();
                            assertNotNull(exceptionBody);
                            assertEquals("Validation failed.", exceptionBody.getMessage());

                        },
                        spec -> System.out.println(spec.returnResult(String.class))
                );

    }


    @Test
    void userController_Update_ShouldNotUpdateDate_BecauseIdAndOldPasswordFalse() throws Exception {
        UserAndTokenResponseDto user = getUser(3);

        UserUpdateRequestDto[] userUpdateArray ={
                UserUpdateRequestDto
                        .builder()
                        .id(null)
                        .oldPassword(null)
                        .newName("Test Test")
                        .newUsername("test@test.test")
                        .newPassword("test")
                        .build(),
                UserUpdateRequestDto
                        .builder()
                        .id(user.getId())
                        .oldPassword(null)
                        .newName("Test Test")
                        .newUsername("test@test.test")
                        .newPassword("test")
                        .build(),
                UserUpdateRequestDto
                        .builder()
                        .id(null)
                        .oldPassword("12345")
                        .newName("Test Test")
                        .newUsername("test@test.test")
                        .newPassword("test")
                        .build()
        };
        for(UserUpdateRequestDto userUpdate: userUpdateArray){
            String userJson = objectMapper.writeValueAsString(userUpdate);

            webTestClient.put()
                    .uri("/api/v1/users/update")
                    .header("Authorization", "Bearer " + user.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(userJson)
                    .exchange()


                    .expectAll(
                            spec -> spec.expectStatus().isBadRequest(),
                            spec -> spec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                            spec -> {
                                EntityExchangeResult<ExceptionBody> result = spec.expectBody(ExceptionBody.class).returnResult();
                                assertNotNull(result);
                                ExceptionBody ex = result.getResponseBody();
                                assertNotNull(ex);
                                assertEquals("Validation failed.", ex.getMessage());
                            },
                            spec -> System.out.println(spec.returnResult(String.class))

                    );
        }

    }

    @Test
    void userController_Update_ShouldNotUpdateDate_BecauseNotForbidden() throws Exception {
        UserAndTokenResponseDto user = getUser(4);

        UserUpdateRequestDto[] userUpdateArray ={
                UserUpdateRequestDto
                        .builder()
                        .id(224L)
                        .oldPassword("12345")
                        .newName("Test Test")
                        .newUsername("test@test.test")
                        .newPassword("test")
                        .build(),
                UserUpdateRequestDto
                        .builder()
                        .id(user.getId())
                        .oldPassword("123456")
                        .newName("Test Test")
                        .newUsername("test@test.test")
                        .newPassword("test")
                        .build()
        };

        for(UserUpdateRequestDto userDto: userUpdateArray){
            String userJson = objectMapper.writeValueAsString(userDto);

            webTestClient.put()
                    .uri("/api/v1/users/update")
                    .header("Authorization", "Bearer " + user.getAccessToken())

                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(userJson)
                    .exchange()
                    .expectAll(
                            spec -> spec.expectStatus().isForbidden(),
                            spec -> {
                                EntityExchangeResult<ExceptionBody> result = spec.expectBody(ExceptionBody.class).returnResult();
                                assertNotNull(result);
                                ExceptionBody ex = result.getResponseBody();
                                assertNotNull(ex);
                                assertEquals("Access, denied", ex.getMessage());
                            },
                            spec -> System.out.println(spec.returnResult(String.class))
                    );

        }
    }

    @Test
    void userController_Update_ShouldUpdateOneFieldDate() throws Exception {


        UserAndTokenResponseDto user = getUser(5);
        UserUpdateRequestDto userUpdate = UserUpdateRequestDto
                .builder()
                .id(user.getId())
                .oldPassword("12345")
                .newName("Test Test")
                .build();
        String userJson = objectMapper.writeValueAsString(userUpdate);
        FluxExchangeResult<String> result = webTestClient.put()
                .uri("/api/v1/users/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userJson)
                .exchange()


                .expectAll(
                        spec -> spec.expectStatus().isOk(),
                        spec -> spec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spec -> System.out.println(spec.returnResult(String.class))
                )
                .returnResult(String.class);

        assertNotNull(result);
        UserResponseDto userResult = objectMapper.readValue(result.getResponseBodyContent(), UserResponseDto.class );
        assertNotNull(userResult);
        assertEquals(user.getId(), userResult.getId());
        assertEquals(userUpdate.getNewName(), userResult.getName());
        assertNotNull(userResult.getListTask());

        LoginRequest loginRequest = new LoginRequest("m57NMah@yahoo.com","12345");
        String log = objectMapper.writeValueAsString(loginRequest);

        FluxExchangeResult<String> returnResult = webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(log)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(String.class);
        assertNotNull(returnResult);
        UserAndTokenResponseDto responseDto = objectMapper.readValue(returnResult.getResponseBodyContent(),UserAndTokenResponseDto.class);
        assertNotNull(responseDto);
        assertEquals(userResult.getId(),responseDto.getId());
        assertEquals(userResult.getName(), responseDto.getName());
        assertEquals(userResult.getListTask(), responseDto.getTasks());



        // второе действие
        UserUpdateRequestDto userUsernameUpdate = UserUpdateRequestDto
                .builder()
                .id(user.getId())
                .oldPassword("12345")
                .newUsername("test@test.test")
                .build();
        String userJson2 = objectMapper.writeValueAsString(userUsernameUpdate);
        FluxExchangeResult<String> result2 = webTestClient.put()
                .uri("/api/v1/users/update")
                .header("Authorization", "Bearer " + responseDto.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userJson2)
                .exchange()


                .expectAll(
                        spec -> spec.expectStatus().isOk(),
                        spec -> spec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spec -> System.out.println(spec.returnResult(String.class))
                )
                .returnResult(String.class);
        assertNotNull(result2);
        UserResponseDto userResult2 = objectMapper.readValue(result2.getResponseBodyContent(), UserResponseDto.class );
        assertNotNull(userResult2);
        assertEquals(user.getId(), userResult2.getId());
        assertEquals(responseDto.getName(), userResult2.getName());
        assertNotNull(userResult.getListTask());

        LoginRequest loginRequest2 = new LoginRequest(userUsernameUpdate.getNewUsername(),userUsernameUpdate.getOldPassword());
        String log2 = objectMapper.writeValueAsString(loginRequest2);
        FluxExchangeResult<String> returnResult2 = webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(log2)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(String.class);
        assertNotNull(returnResult2);
        UserAndTokenResponseDto responseDto2 = objectMapper.readValue(returnResult2.getResponseBodyContent(),UserAndTokenResponseDto.class);
        assertNotNull(responseDto2);
        assertEquals(userResult.getId(),responseDto2.getId());
        assertEquals(userResult.getName(), responseDto2.getName());
        assertEquals(userResult.getListTask(), responseDto2.getTasks());


        // действие три
        UserUpdateRequestDto userPasswordUpdate = UserUpdateRequestDto
                .builder()
                .id(user.getId())
                .oldPassword("12345")
                .newPassword("test")
                .build();
        String userJson3 = objectMapper.writeValueAsString(userPasswordUpdate);
        FluxExchangeResult<String> result3 = webTestClient.put()
                .uri("/api/v1/users/update")
                .header("Authorization", "Bearer " + responseDto2.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userJson3)
                .exchange()


                .expectAll(
                        spec -> spec.expectStatus().isOk(),
                        spec -> spec.expectHeader().contentType(MediaType.APPLICATION_JSON),
                        spec -> System.out.println(spec.returnResult(String.class))
                )
                .returnResult(String.class);
        assertNotNull(result3);
        UserResponseDto userResult3 = objectMapper.readValue(result3.getResponseBodyContent(), UserResponseDto.class );
        assertNotNull(userResult3);
        assertEquals(user.getId(), userResult3.getId());
        assertEquals(responseDto.getName(), userResult3.getName());
        assertNotNull(userResult.getListTask());

        LoginRequest loginRequest3 = new LoginRequest(userUsernameUpdate.getNewUsername(),userPasswordUpdate.getNewPassword());
        String log3 = objectMapper.writeValueAsString(loginRequest3);

        FluxExchangeResult<String> returnResult3 = webTestClient.post()
                .uri("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(log3)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .returnResult(String.class);
        assertNotNull(returnResult3);
        UserAndTokenResponseDto responseDto3 = objectMapper.readValue(returnResult3.getResponseBodyContent(),UserAndTokenResponseDto.class);
        assertNotNull(responseDto3);
        assertEquals(userResult.getId(),responseDto3.getId());
        assertEquals(userResult.getName(), responseDto3.getName());
        assertEquals(userResult.getListTask(), responseDto3.getTasks());
    }
    @Test
    void userController_createTask_ShouldValidationCheckDataAndSaveInDatabase(){

    }

}