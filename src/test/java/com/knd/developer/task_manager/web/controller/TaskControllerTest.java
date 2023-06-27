package com.knd.developer.task_manager.web.controller;

import com.knd.developer.task_manager.IntegrationTestBase;
import com.knd.developer.task_manager.domain.exception.ExceptionBody;
import com.knd.developer.task_manager.domain.task.PriorityTask;
import com.knd.developer.task_manager.domain.task.Status;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import com.knd.developer.task_manager.web.dto.user.response.UserAndTokenResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TaskControllerTest extends IntegrationTestBase {

    //Проводит валидацию данных, если данные валидны обновляет данные таска и сохраняет их
    @Test
    void taskController_Update_ShouldSaveInBD() throws Exception {
        UserAndTokenResponseDto user = getUser(1);

        TaskDto updateTest = TaskDto.builder()
                .id(user.getTasks().get(0).getId())
                .title("TestUpdate")
                .description("TestUpdate")
                .status(Status.IN_PROGRESS)
                .priority(PriorityTask.HIGH)
                .expirationDate(LocalDateTime.now().plus(4, ChronoUnit.MONTHS).toString())
                .build();

        EntityExchangeResult<TaskDto> returnResult = webTestClient.put()
                .uri("/api/v1/tasks/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateTest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectAll(spec -> System.out.println(spec.returnResult(String.class)))
                .expectBody(TaskDto.class).returnResult();

        EntityExchangeResult<List> result = webTestClient.get()
                .uri("/api/v1/users/" + user.getId() + "/tasks")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(List.class).returnResult();
        assertNotNull(result);

        List<Map<String, Object>> responseList = result.getResponseBody();

        List<TaskDto> tasks = responseList.stream()
                .map(responseMap -> objectMapper.convertValue(responseMap, TaskDto.class))
                .collect(Collectors.toList());
        assertEquals(updateTest.getId(), tasks.get(0).getId());
        assertEquals(updateTest.getTitle(), tasks.get(0).getTitle());
        assertEquals(updateTest.getDescription(), tasks.get(0).getDescription());
        assertEquals(updateTest.getStatus(), tasks.get(0).getStatus());
        assertEquals(updateTest.getPriority(), tasks.get(0).getPriority());
        assertNotNull(tasks.get(0).getExpirationDate());
    }

    //попытка изменить таск не принадлежащий данному пользователю, возвращается ответ со статусом Forbidden
    @Test
    void taskController_Update_ShouldReturnStatusIsForbidden_BecauseAttemptToUpdateAnotherUserSTask() throws Exception {
        UserAndTokenResponseDto user = getUser(10);

        TaskDto updateTest = TaskDto.builder()
                .id(815L)
                .title("TestUpdate")
                .description("TestUpdate")
                .status(Status.IN_PROGRESS)
                .priority(PriorityTask.HIGH)
                .expirationDate(LocalDateTime.now().plus(4, ChronoUnit.MONTHS).toString())
                .build();

        webTestClient.put()
                .uri("/api/v1/tasks/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateTest)
                .exchange()
                .expectStatus().isForbidden()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectAll(
                        spec -> {
                            EntityExchangeResult<ExceptionBody> exceptionBodyEntityExchangeResult = spec
                                    .expectBody(ExceptionBody.class)
                                    .returnResult();
                            assertNotNull(exceptionBodyEntityExchangeResult);
                            ExceptionBody exceptionBody = exceptionBodyEntityExchangeResult.getResponseBody();
                            assertNotNull(exceptionBody);
                        },
                        spec -> System.out.println(spec.returnResult(String.class)));


        EntityExchangeResult<List> result = webTestClient.get()
                .uri("/api/v1/users/" + user.getId() + "/tasks")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(List.class).returnResult();
        assertNotNull(result);

        List<Map<String, Object>> responseList = result.getResponseBody();

        List<TaskDto> tasks = responseList.stream()
                .map(responseMap -> objectMapper.convertValue(responseMap, TaskDto.class))
                .collect(Collectors.toList());
        assertNotEquals(updateTest.getId(), tasks.get(0).getId());
        assertNotEquals(updateTest.getTitle(), tasks.get(0).getTitle());
        assertNotEquals(updateTest.getDescription(), tasks.get(0).getDescription());
        assertNotEquals(updateTest.getStatus(), tasks.get(0).getStatus());
        assertNotEquals(updateTest.getPriority(), tasks.get(0).getPriority());
        assertNotNull(tasks.get(0).getExpirationDate());
    }

    //попытка обновить данные при том что данные пустые, не обновит ни каких данных
    @Test
    void taskController_Update_ShouldNotUpdate_BecauseDataIsNull() throws Exception {
        UserAndTokenResponseDto user = getUser(10);
        TaskDto oldTask = user.getTasks().get(0);

        TaskDto updateTest = TaskDto.builder()
                .id(oldTask.getId())
                .title(null)
                .description(null)
                .status(null)
                .priority(null)
                .expirationDate(null)
                .build();

        EntityExchangeResult<TaskDto> resultTaskDto = webTestClient.put()
                .uri("/api/v1/tasks/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateTest)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectAll(
                        spec -> {
                            EntityExchangeResult<ExceptionBody> exceptionBodyEntityExchangeResult = spec
                                    .expectBody(ExceptionBody.class)
                                    .returnResult();
                            assertNotNull(exceptionBodyEntityExchangeResult);
                            ExceptionBody exceptionBody = exceptionBodyEntityExchangeResult.getResponseBody();
                            assertNotNull(exceptionBody);
                        },
                        spec -> System.out.println(spec.returnResult(String.class)))
                .expectBody(TaskDto.class).returnResult();
        TaskDto resultTask = resultTaskDto.getResponseBody();
//oldTask
        assertEquals(oldTask.getId(), resultTask.getId());
        assertEquals(oldTask.getTitle(), resultTask.getTitle());
        assertEquals(oldTask.getDescription(), resultTask.getDescription());
        assertEquals(oldTask.getStatus(), resultTask.getStatus());
        assertEquals(oldTask.getPriority(), resultTask.getPriority());
        assertEquals(oldTask.getExpirationDate(), resultTask.getExpirationDate());

    }

    //Попытки изменить данные по-одному полю
    @Test
    void taskController_Update_ShouldChangeTheDataOneByOne() throws Exception {
        UserAndTokenResponseDto user = getUser(10);
        TaskDto oldTask = user.getTasks().get(1);


        //Попытка изменить title
        TaskDto updateTest1 = TaskDto.builder()
                .id(oldTask.getId())
                .title("Update title")
                .description(null)
                .status(null)
                .priority(null)
                .expirationDate(null)
                .build();


        EntityExchangeResult<TaskDto> resultTaskDto = webTestClient.put()
                .uri("/api/v1/tasks/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateTest1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectAll(
                        spec -> {
                            EntityExchangeResult<ExceptionBody> exceptionBodyEntityExchangeResult = spec
                                    .expectBody(ExceptionBody.class)
                                    .returnResult();
                            assertNotNull(exceptionBodyEntityExchangeResult);
                            ExceptionBody exceptionBody = exceptionBodyEntityExchangeResult.getResponseBody();
                            assertNotNull(exceptionBody);
                        })
                .expectBody(TaskDto.class).returnResult();
        TaskDto resultTask = resultTaskDto.getResponseBody();

        assertEquals(oldTask.getId(), resultTask.getId());

        assertNotEquals(oldTask.getTitle(), resultTask.getTitle());
        assertEquals(updateTest1.getTitle(), resultTask.getTitle());

        assertEquals(oldTask.getDescription(), resultTask.getDescription());
        assertEquals(oldTask.getStatus(), resultTask.getStatus());
        assertEquals(oldTask.getPriority(), resultTask.getPriority());
        assertEquals(oldTask.getExpirationDate(), resultTask.getExpirationDate());

        // Попытка изменить description
        TaskDto updateTest2 = TaskDto.builder()
                .id(oldTask.getId())
                .title(null)
                .description("Update description")
                .status(null)
                .priority(null)
                .expirationDate(null)
                .build();


        EntityExchangeResult<TaskDto> resultTaskDto2 = webTestClient.put()
                .uri("/api/v1/tasks/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateTest2)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectAll(
                        spec -> {
                            EntityExchangeResult<ExceptionBody> exceptionBodyEntityExchangeResult = spec
                                    .expectBody(ExceptionBody.class)
                                    .returnResult();
                            assertNotNull(exceptionBodyEntityExchangeResult);
                            ExceptionBody exceptionBody = exceptionBodyEntityExchangeResult.getResponseBody();
                            assertNotNull(exceptionBody);
                        })
                .expectBody(TaskDto.class).returnResult();
        TaskDto resultTask2 = resultTaskDto2.getResponseBody();

        assertEquals(oldTask.getId(), resultTask2.getId());
        assertEquals(resultTask.getTitle(), resultTask2.getTitle());

        assertNotEquals(oldTask.getDescription(), resultTask2.getDescription());
        assertEquals(updateTest2.getDescription(), resultTask2.getDescription());

        assertEquals(oldTask.getStatus(), resultTask2.getStatus());
        assertEquals(oldTask.getPriority(), resultTask2.getPriority());
        assertEquals(oldTask.getExpirationDate(), resultTask2.getExpirationDate());

        // Попытка изменить status
        TaskDto updateTest3 = TaskDto.builder()
                .id(oldTask.getId())
                .title(null)
                .description(null)
                .status(Status.DONE)
                .priority(null)
                .expirationDate(null)
                .build();


        EntityExchangeResult<TaskDto> resultTaskDto3 = webTestClient.put()
                .uri("/api/v1/tasks/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateTest3)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectAll(
                        spec -> {
                            EntityExchangeResult<ExceptionBody> exceptionBodyEntityExchangeResult = spec
                                    .expectBody(ExceptionBody.class)
                                    .returnResult();
                            assertNotNull(exceptionBodyEntityExchangeResult);
                            ExceptionBody exceptionBody = exceptionBodyEntityExchangeResult.getResponseBody();
                            assertNotNull(exceptionBody);
                        })
                .expectBody(TaskDto.class).returnResult();
        TaskDto resultTask3 = resultTaskDto3.getResponseBody();

        assertEquals(oldTask.getId(), resultTask3.getId());
        assertEquals(resultTask.getTitle(), resultTask3.getTitle());
        assertEquals(resultTask2.getDescription(), resultTask3.getDescription());

        assertNotEquals(oldTask.getStatus(), resultTask3.getStatus());
        assertEquals(updateTest3.getStatus(), resultTask3.getStatus());

        assertEquals(oldTask.getPriority(), resultTask3.getPriority());
        assertEquals(oldTask.getExpirationDate(), resultTask3.getExpirationDate());

        // Попытка изменить priority
        TaskDto updateTest4 = TaskDto.builder()
                .id(oldTask.getId())
                .title(null)
                .description(null)
                .status(null)
                .priority(PriorityTask.HIGH)
                .expirationDate(null)
                .build();


        EntityExchangeResult<TaskDto> resultTaskDto4 = webTestClient.put()
                .uri("/api/v1/tasks/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateTest4)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectAll(
                        spec -> {
                            EntityExchangeResult<ExceptionBody> exceptionBodyEntityExchangeResult = spec
                                    .expectBody(ExceptionBody.class)
                                    .returnResult();
                            assertNotNull(exceptionBodyEntityExchangeResult);
                            ExceptionBody exceptionBody = exceptionBodyEntityExchangeResult.getResponseBody();
                            assertNotNull(exceptionBody);
                        })
                .expectBody(TaskDto.class).returnResult();
        TaskDto resultTask4 = resultTaskDto4.getResponseBody();

        assertEquals(oldTask.getId(), resultTask4.getId());
        assertEquals(resultTask.getTitle(), resultTask4.getTitle());
        assertEquals(resultTask2.getDescription(), resultTask4.getDescription());
        assertEquals(resultTask3.getStatus(), resultTask4.getStatus());

        assertNotEquals(oldTask.getPriority(), resultTask4.getPriority());
        assertEquals(updateTest4.getPriority(), resultTask4.getPriority());

        assertEquals(oldTask.getExpirationDate(), resultTask4.getExpirationDate());

        // Попытка изменить expirationDate
        TaskDto updateTest5 = TaskDto.builder()
                .id(oldTask.getId())
                .title(null)
                .description(null)
                .status(null)
                .priority(null)
                .expirationDate(LocalDateTime.now().plus(12, ChronoUnit.MONTHS).toString())
                .build();


        EntityExchangeResult<TaskDto> resultTaskDto5 = webTestClient.put()
                .uri("/api/v1/tasks/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateTest5)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectAll(
                        spec -> {
                            EntityExchangeResult<ExceptionBody> exceptionBodyEntityExchangeResult = spec
                                    .expectBody(ExceptionBody.class)
                                    .returnResult();
                            assertNotNull(exceptionBodyEntityExchangeResult);
                            ExceptionBody exceptionBody = exceptionBodyEntityExchangeResult.getResponseBody();
                            assertNotNull(exceptionBody);
                        })
                .expectBody(TaskDto.class).returnResult();
        TaskDto resultTask5 = resultTaskDto5.getResponseBody();

        assertEquals(oldTask.getId(), resultTask5.getId());
        assertEquals(resultTask.getTitle(), resultTask5.getTitle());
        assertEquals(resultTask2.getDescription(), resultTask5.getDescription());
        assertEquals(resultTask3.getStatus(), resultTask5.getStatus());
        assertEquals(resultTask4.getPriority(), resultTask5.getPriority());

        assertNotEquals(oldTask.getExpirationDate(), resultTask5.getExpirationDate());
        assertEquals(updateTest5.getExpirationDate().substring(0, 26), resultTask5.getExpirationDate().substring(0, 26));
    }

    //попытка передать не валидные данные, вернет сообщение со статусом IsBadRequest
    @Test
    void taskController_Update_ShouldReturnStatusBadRequest_BecauseDateNotValidation() throws Exception {
        UserAndTokenResponseDto user = getUser(10);

        TaskDto oldTask = user.getTasks().get(2);


        TaskDto[] updateTest = {
                TaskDto.builder()
                        .id(oldTask.getId())
                        .title(generateRandomString(40))
                        .description("TestUpdate")
                        .status(Status.IN_PROGRESS)
                        .priority(PriorityTask.HIGH)
                        .expirationDate(LocalDateTime.now().plus(4, ChronoUnit.MONTHS).toString())
                        .build(),
                TaskDto.builder()
                        .id(oldTask.getId())
                        .title("TestUpd[}'>ate")
                        .description("TestUpdate")
                        .status(Status.IN_PROGRESS)
                        .priority(PriorityTask.HIGH)
                        .expirationDate(LocalDateTime.now().plus(4, ChronoUnit.MONTHS).toString())
                        .build(),
                TaskDto.builder()
                        .id(oldTask.getId())
                        .title("TestUpdate")
                        .description(generateRandomString(300))
                        .status(Status.IN_PROGRESS)
                        .priority(PriorityTask.HIGH)
                        .expirationDate(LocalDateTime.now().plus(4, ChronoUnit.MONTHS).toString())
                        .build(),
                TaskDto.builder()
                        .id(oldTask.getId())
                        .title("TestUpdate")
                        .description("TestUpdat>{]'e}")
                        .status(Status.IN_PROGRESS)
                        .priority(PriorityTask.HIGH)
                        .expirationDate(LocalDateTime.now().plus(4, ChronoUnit.MONTHS).toString())
                        .build()};

        for (TaskDto taskDto : updateTest) {
            webTestClient.put()
                    .uri("/api/v1/tasks/update")
                    .header("Authorization", "Bearer " + user.getAccessToken())
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(taskDto)
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectAll(
                            spec -> {
                                EntityExchangeResult<ExceptionBody> exceptionBodyEntityExchangeResult = spec
                                        .expectBody(ExceptionBody.class)
                                        .returnResult();
                                assertNotNull(exceptionBodyEntityExchangeResult);
                                ExceptionBody exceptionBody = exceptionBodyEntityExchangeResult.getResponseBody();
                                assertNotNull(exceptionBody);
                            },
                            spec -> System.out.println(spec.returnResult(String.class)));
        }
    }

    //Попытка обновить таску без id, вернет сообщение со статусом IsForbidden, так как приложение не сможет найти, кому принадлежит таска
    @Test
    void taskController_Update_Should() throws Exception {
        UserAndTokenResponseDto user = getUser(1);

        TaskDto updateTest = TaskDto.builder()
                .id(null)
                .title("TestUpdate")
                .description("TestUpdate")
                .status(Status.IN_PROGRESS)
                .priority(PriorityTask.HIGH)
                .expirationDate(LocalDateTime.now().plus(4, ChronoUnit.MONTHS).toString())
                .build();

        EntityExchangeResult<TaskDto> returnResult = webTestClient.put()
                .uri("/api/v1/tasks/update")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updateTest)
                .exchange()
                .expectStatus().isForbidden()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectAll(
                        spec -> {
                            EntityExchangeResult<ExceptionBody> exceptionBodyEntityExchangeResult = spec
                                    .expectBody(ExceptionBody.class)
                                    .returnResult();
                            assertNotNull(exceptionBodyEntityExchangeResult);
                            ExceptionBody exceptionBody = exceptionBodyEntityExchangeResult.getResponseBody();
                            assertNotNull(exceptionBody);
                        },
                        spec -> System.out.println(spec.returnResult(String.class)))
                .expectBody(TaskDto.class).returnResult();

    }

    //Ищет в БД таск и возврощает его
    @Test
    void taskController_GetTask_ShouldReturnTaskFromBD() throws Exception {
        UserAndTokenResponseDto user = getUser(10);

        TaskDto oldTask = user.getTasks().get(3);

        EntityExchangeResult<TaskDto> result = webTestClient.get()
                .uri("/api/v1/tasks/" + oldTask.getId() + "/get")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isOk()
                .expectAll(spec -> System.out.println(spec.returnResult(String.class)))
                .expectBody(TaskDto.class).returnResult();
        TaskDto resultTask = result.getResponseBody();
        assertNotNull(resultTask);
        assertEquals(oldTask.getId(), resultTask.getId());
        assertEquals(oldTask.getTitle(), resultTask.getTitle());
        assertEquals(oldTask.getDescription(), resultTask.getDescription());
        assertEquals(oldTask.getStatus(), resultTask.getStatus());
        assertEquals(oldTask.getPriority(), resultTask.getPriority());
        assertEquals(oldTask.getExpirationDate(), resultTask.getExpirationDate());
    }

    //попытка доступа к чужому task, вернет сообщение со статусом IsForbidden
    @Test
    void taskController_GetTask_ShouldReturnStatusResponseIsForbidden() throws Exception {
        UserAndTokenResponseDto user = getUser(10);

        webTestClient.get()
                .uri("/api/v1/tasks/" + 972 + "/get")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectStatus().isForbidden()
                .expectAll(
                        spec -> {
                            EntityExchangeResult<ExceptionBody> exceptionBodyEntityExchangeResult = spec
                                    .expectBody(ExceptionBody.class)
                                    .returnResult();
                            assertNotNull(exceptionBodyEntityExchangeResult);
                            ExceptionBody exceptionBody = exceptionBodyEntityExchangeResult.getResponseBody();
                            assertNotNull(exceptionBody);
                        },
                        spec -> System.out.println(spec.returnResult(String.class)));
    }
    //попытка не передавать id task, вернет сообщение со статусом IsNotFound
    @Test
    void taskController_GetTask_ShouldReturnStatusResponseIsNotFound_BecauseThereIsNoTaskId() throws Exception {
        UserAndTokenResponseDto user = getUser(10);

        webTestClient.get()
                .uri("/api/v1/tasks/"+""+"/get")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectAll(spec -> System.out.println(spec.returnResult(String.class)));
    }
    //удаляет task из БД по id
    @Test
    void taskController_DeleteTask_ShouldReturnStatusResponseIsForbidden_BecauseAttemptDeleteSomeoneElseIsTask() throws Exception {
        UserAndTokenResponseDto user = getUser(10);

        TaskDto oldTask = user.getTasks().get(4);

        webTestClient.delete()
                .uri("/api/v1/tasks/"+oldTask.getId()+"/delete")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectAll(spec -> System.out.println(spec.returnResult(String.class)));

        EntityExchangeResult<List> result = webTestClient.get()
                .uri("/api/v1/users/" + user.getId() + "/tasks")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(List.class).returnResult();
        assertNotNull(result);
        List<Map<String, Object>> responseList = result.getResponseBody();

        List<TaskDto> tasks = responseList.stream()
                .map(responseMap -> objectMapper.convertValue(responseMap, TaskDto.class))
                .collect(Collectors.toList());
        assertEquals(user.getTasks().size(),tasks.size()+1);
    }

    //попытка удалить чужой task, вернет сообщение со статусом isForbidden
    @Test
    void taskController_DeleteTask_ShouldReturnStatusResponseIsForbidden_BecauseAttemptDeleteNotYourTask() throws Exception {
        UserAndTokenResponseDto user = getUser(10);


        webTestClient.delete()
                .uri("/api/v1/tasks/"+105+"/delete")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isForbidden()
                .expectAll(
                        spec -> {
                            EntityExchangeResult<ExceptionBody> exceptionBodyEntityExchangeResult = spec
                                    .expectBody(ExceptionBody.class)
                                    .returnResult();
                            assertNotNull(exceptionBodyEntityExchangeResult);
                            ExceptionBody exceptionBody = exceptionBodyEntityExchangeResult.getResponseBody();
                            assertNotNull(exceptionBody);
                        },
                        spec -> System.out.println(spec.returnResult(String.class)));

        EntityExchangeResult<List> result = webTestClient.get()
                .uri("/api/v1/users/" + user.getId() + "/tasks")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(List.class).returnResult();
        assertNotNull(result);
        List<Map<String, Object>> responseList = result.getResponseBody();

        List<TaskDto> tasks = responseList.stream()
                .map(responseMap -> objectMapper.convertValue(responseMap, TaskDto.class))
                .collect(Collectors.toList());
        assertEquals(user.getTasks().size(),tasks.size());

    }
    //попытка удалить task без передачи id(task)
    @Test
    void taskController_DeleteTask_ShouldReturnStatusResponseIsNotFound_BecauseNoTaskId() throws Exception {
        UserAndTokenResponseDto user = getUser(10);


        webTestClient.delete()
                .uri("/api/v1/tasks/"+""+"/delete")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectAll(spec -> System.out.println(spec.returnResult(String.class)));

        EntityExchangeResult<List> result = webTestClient.get()
                .uri("/api/v1/users/" + user.getId() + "/tasks")
                .header("Authorization", "Bearer " + user.getAccessToken())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(List.class).returnResult();
        assertNotNull(result);
        List<Map<String, Object>> responseList = result.getResponseBody();

        List<TaskDto> tasks = responseList.stream()
                .map(responseMap -> objectMapper.convertValue(responseMap, TaskDto.class))
                .collect(Collectors.toList());
        assertEquals(user.getTasks().size(),tasks.size());

    }


}