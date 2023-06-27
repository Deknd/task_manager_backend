package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.exception.ResourceNotFoundException;
import com.knd.developer.task_manager.domain.exception.ResourcesMappingException;
import com.knd.developer.task_manager.domain.task.PriorityTask;
import com.knd.developer.task_manager.domain.task.Status;
import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.repository.TaskRepository;
import com.knd.developer.task_manager.service.props.PatternString;
import com.knd.developer.task_manager.service.props.TaskProperties;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.xmlunit.builder.Input;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application.yml")
class TaskServiceImplTest {
    private static Yaml yaml;
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;
    @Mock
    private PatternString pattern;
    @Mock
    private TaskProperties properties;

    private static Long max_length_title;
    private static Long max_length_description;
    private static Pattern EMAIL_PATTERN;
    private static Pattern FORBIDDEN_JS_CHARS_PATTERN;

    @BeforeAll
    static void test() throws FileNotFoundException {
        InputStream stream = new FileInputStream("src/main/resources/application.yml");
        yaml = new Yaml();
        Map<String, Object> data = yaml.load(stream);
        Object ob = data.get("entity");
        if (ob instanceof Map) {
            Map<String, Object> entity = (Map<String, Object>) ob;
            Object task = entity.get("task");
            if (task instanceof Map) {
                Map<String, String> taskProps = (Map<String, String>) task;

                Object stringTitle = taskProps.get("max_length_title");
                Integer testInt1 = null;
                if (stringTitle instanceof Integer) {
                    testInt1 = (Integer) stringTitle;
                } else if (stringTitle instanceof String) {
                    try {
                        testInt1 = (Integer.parseInt((String) stringTitle));
                    } catch (NumberFormatException e) {
                        System.out.println("Все плохо");
                    }
                }
                max_length_title = testInt1.longValue();
                Object stringDescription = taskProps.get("max_length_description");

                Integer testInt2 = null;
                if (stringDescription instanceof Integer) {
                    testInt2 = (Integer) stringDescription;
                } else if (stringDescription instanceof String) {
                    try {
                        testInt2 = (Integer.parseInt((String) stringDescription));
                    } catch (NumberFormatException e) {
                        System.out.println("Все плохо");
                    }
                }
                max_length_description = testInt2.longValue();
            }

            Object pattern = entity.get("pattern");
            if (pattern instanceof Map) {
                Map<String, String> patternProps = (Map<String, String>) pattern;
                EMAIL_PATTERN = Pattern.compile(patternProps.get("email_pattern"));
                FORBIDDEN_JS_CHARS_PATTERN = Pattern.compile(patternProps.get("forbidden_js_chars_pattern"));
            }

        }

    }


    //Возвращает Task по данному id
    @Test
    void getById_ShouldReturnTask() { //IfNotNullElseThrowException
        Long id = id_l();
        Task task = mock(Task.class);
        when(taskRepository.findById(eq(id))).thenReturn(Optional.of(task));

        Task result = taskService.getById(id);

        assertEquals(task, result);
    }

    //выкидывает ошибку, так как task под данным id нет
    @Test
    void getById_ShouldTrowsResourceNotFoundException_BecauseNotTask() {
        Long id = id_l();
        Task task = mock(Task.class);
        when(taskRepository.findById(eq(id))).thenReturn(Optional.of(task));

        assertThrows(ResourceNotFoundException.class, () -> taskService.getById(12L));
    }

    //Делает запрос в таск репозиторий и возвращает лист тасков
    @Test
    void getAllByUserId_ShouldReturnAllTaskGivenUser() { //IfTaskNoReturnEmptyList

        Long id = id_l();
        List<Task> tasks = List.of(mock(Task.class), mock(Task.class), mock(Task.class), mock(Task.class));
        when(taskRepository.findAllByUserId(eq(id))).thenReturn(tasks);

        List<Task> result = taskService.getAllTasksByUserId(id);

        assertEquals(tasks, result);
    }

    //Возвращает пустой лист, если у пользователя нет tasks
    @Test
    void getAllByUserId_ShouldListEmpty_BecauseUserHasNoTasks() { //IfTaskNoReturnEmptyList

        Long id = id_l();

        List<Task> tasks = new ArrayList<>();
        when(taskRepository.findAllByUserId(eq(id))).thenReturn(tasks);

        List<Task> result = taskService.getAllTasksByUserId(id);

        assertEquals(tasks, result);

    }

    //Не меняется статус, так как даты завершения нет
    @Test
    void getAllByUserId_ShouldNoChangeStatus_BecauseExpirationDateNull() { //IfTaskNoReturnEmptyList

        Long id = id_l();

        List<Task> tasks = List.of(
                Task.builder()
                        .status(Status.TODO)
                        .build(),
                Task.builder()
                        .status(Status.IN_PROGRESS)
                        .build(),
                Task.builder()
                        .status(Status.DONE)
                        .build(),
                Task.builder()
                        .status(Status.FAILED)
                        .build()
        );
        when(taskRepository.findAllByUserId(eq(id))).thenReturn(tasks);

        List<Task> result = taskService.getAllTasksByUserId(id);

        for (int i = 0; i < tasks.size(); i++) {

            assertEquals(tasks.get(i), result.get(i));

        }

    }

    //Не меняется статус, так как даты завершения позже чем сейчас
    @Test
    void getAllByUserId_ShouldNoChangeStatus_BecauseExpirationDateBeforeNow() { //IfTaskNoReturnEmptyList

        Long id = id_l();

        List<Task> tasks = List.of(
                Task.builder()
                        .status(Status.TODO)
                        .expirationDate(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                        .build(),
                Task.builder()
                        .status(Status.IN_PROGRESS)
                        .expirationDate(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                        .build(),
                Task.builder()
                        .status(Status.DONE)
                        .expirationDate(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                        .build(),
                Task.builder()
                        .status(Status.FAILED)
                        .expirationDate(LocalDateTime.now().plus(1, ChronoUnit.DAYS))
                        .build()
        );
        when(taskRepository.findAllByUserId(eq(id))).thenReturn(tasks);

        List<Task> result = taskService.getAllTasksByUserId(id);

        for (int i = 0; i < tasks.size(); i++) {

            assertEquals(tasks.get(i), result.get(i));

        }

    }

    //Меняется статус на Failed, если дата дедлайна просрочена, но не меняется, если таск выполнен
    @Test
    void getAllByUserId_ShouldChangeTheStatusOfUnfinishedTasks() { //IfTaskNoReturnEmptyList

        Long id = id_l();

        List<Task> tasks = List.of(
                Task.builder()
                        .status(Status.TODO)
                        .expirationDate(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
                        .build(),
                Task.builder()
                        .status(Status.IN_PROGRESS)
                        .expirationDate(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
                        .build(),
                Task.builder()
                        .status(Status.DONE)
                        .expirationDate(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
                        .build(),
                Task.builder()
                        .status(Status.FAILED)
                        .expirationDate(LocalDateTime.now().minus(1, ChronoUnit.DAYS))
                        .build()
        );
        when(taskRepository.findAllByUserId(eq(id))).thenReturn(tasks);

        List<Task> result = taskService.getAllTasksByUserId(id);

        for (int i = 0; i < tasks.size(); i++) {
            if (result.get(i).getExpirationDate().isBefore(LocalDateTime.now())) {
                if (result.get(i).getStatus() != Status.DONE) {
                    assertEquals(Status.FAILED, result.get(i).getStatus());

                }
            }

        }

    }


    //Проверяет новые данные, проверяет время дедлайна, сохраняет новые данные в бд, возвращает обновленый таск
    @Test
    void update_ShouldChangeTheOldTask() {

        Long id = id_l();
        Long userId = id_l();
        Task oldTask = Task.builder()
                .id(id)
                .user_id(userId)
                .title("Old title")
                .description("Old description")
                .status(Status.TODO)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(24, ChronoUnit.HOURS), ZoneId.systemDefault()))
                .priority(PriorityTask.HIGH)
                .build();


        TaskDto newTask = TaskDto.builder()
                .id(id)
                .title("New title")
                .description("New description")
                .status(Status.IN_PROGRESS)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .priority(PriorityTask.STANDARD)
                .build();

        when(taskRepository.findById(eq(id))).thenReturn(Optional.of(copyTask(oldTask)));
        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(properties.getMax_length_title()).thenReturn(max_length_title);
        when(properties.getMax_length_description()).thenReturn(max_length_description);

        Task result = taskService.update(newTask);

        verify(taskRepository).update(any(Task.class));
        assertNotNull(result);
        assertEquals(oldTask.getId(), result.getId());
        assertEquals(oldTask.getUser_id(), result.getUser_id());
        assertNotEquals(oldTask.getTitle(), result.getTitle());
        assertNotEquals(oldTask.getDescription(), result.getDescription());
        assertNotEquals(oldTask.getStatus().toString(), result.getStatus().toString());
        assertNotEquals(oldTask.getPriority().toString(), result.getPriority().toString());
        assertNotEquals(oldTask.getExpirationDate(), result.getExpirationDate());

    }

    //Если передать TaskDto с id(Task) которого нет в БД, выбросит исключение ResourceNotFoundException
    @Test
    void update_ShouldTrowsExceptionResourceNotFoundException_BecauseNotTaskInBD() {
        TaskDto newTask = TaskDto.builder()
                .build();
        when(taskRepository.findById(any())).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> taskService.update(newTask));
    }

    //Если пробовать передать не валидные данные, то выбросит исключение ResourcesMappingException
    @Test
    void update_ShouldTrowsException_BecauseDataNotValidation() {

        Long id = id_l();
        Long userId = id_l();

        Task oldTask = Task.builder()
                .id(id)
                .user_id(userId)
                .title("Old title")
                .description("Old description")
                .status(Status.TODO)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(24, ChronoUnit.HOURS), ZoneId.systemDefault()))
                .priority(PriorityTask.HIGH)
                .build();

        StringBuilder stTitle = new StringBuilder();
        for (int i = 0; i < max_length_title; i++) {
            stTitle.append("er");
        }
        TaskDto newTask = TaskDto.builder()
                .id(id)
                .title(stTitle.toString())
                .description("New description")
                .status(Status.IN_PROGRESS)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .priority(PriorityTask.STANDARD)
                .build();

        TaskDto newTask2 = TaskDto.builder()
                .id(id)
                .title("New ti ' <> []")
                .description("New description")
                .status(Status.IN_PROGRESS)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .priority(PriorityTask.STANDARD)
                .build();

        StringBuilder st = new StringBuilder();
        for (int i = 0; i < max_length_description; i++) {
            st.append("New description ");
        }
        TaskDto newTask3 = TaskDto.builder()
                .id(id)
                .title("New title")
                .description(st.toString())
                .status(Status.IN_PROGRESS)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .priority(PriorityTask.STANDARD)
                .build();

        TaskDto newTask4 = TaskDto.builder()
                .id(id)
                .title("New title")
                .description("New d<esc[ript]ion> ' ' '")
                .status(Status.IN_PROGRESS)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .priority(PriorityTask.STANDARD)
                .build();


        when(taskRepository.findById(eq(id))).thenReturn(Optional.of(copyTask(oldTask)));
        when(properties.getMax_length_title()).thenReturn(max_length_title);
        when(properties.getMax_length_description()).thenReturn(max_length_description);
        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);


        assertThrows(ResourcesMappingException.class, () -> taskService.update(newTask));
        assertThrows(ResourcesMappingException.class, () -> taskService.update(newTask2));
        assertThrows(ResourcesMappingException.class, () -> taskService.update(newTask3));
        assertThrows(ResourcesMappingException.class, () -> taskService.update(newTask4));


    }


    //Если принимаемый TaskDto пустой( кроме id(Task)), то ни каких изменений в таске не будет
    @Test
    void update_ShouldNoChangeOldTask_BecauseTaskDtoIsEmpty() {

        Long id = id_l();
        Long userId = id_l();

        Task oldTask = Task.builder()
                .id(id)
                .user_id(userId)
                .title("Old title")
                .description("Old description")
                .status(Status.TODO)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(24, ChronoUnit.HOURS), ZoneId.systemDefault()))
                .priority(PriorityTask.HIGH)
                .build();

        TaskDto newTask = TaskDto.builder()
                .id(id)
                .build();

        when(taskRepository.findById(eq(id))).thenReturn(Optional.of(copyTask(oldTask)));

        Task result = taskService.update(newTask);

        verify(taskRepository).update(any(Task.class));
        assertNotNull(result);
        assertEquals(oldTask.getId(), result.getId());
        assertEquals(oldTask.getUser_id(), result.getUser_id());
        assertEquals(oldTask.getTitle(), result.getTitle());
        assertEquals(oldTask.getDescription(), result.getDescription());
        assertEquals(oldTask.getStatus().toString(), result.getStatus().toString());
        assertEquals(oldTask.getPriority().toString(), result.getPriority().toString());
        assertEquals(oldTask.getExpirationDate(), result.getExpirationDate());

    }

    //Можно менять данные по одному полю, если приходят TaskDto с id(Task) и каким то заполненным полем
    @Test
    void update_ShouldChangeOneDate_BecauseDataArrivesOneAtATime() {
        Long id = id_l();
        Long userId = id_l();

        Task oldTask = Task.builder()
                .id(id)
                .user_id(userId)
                .title("Old title")
                .description("Old description")
                .status(Status.TODO)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(24, ChronoUnit.HOURS), ZoneId.systemDefault()))
                .priority(PriorityTask.HIGH)
                .build();

        when(taskRepository.findById(eq(id))).thenReturn(Optional.of(copyTask(oldTask)));
        when(properties.getMax_length_title()).thenReturn(max_length_title);
        when(properties.getMax_length_description()).thenReturn(max_length_description);
        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);


        TaskDto taskDto1 = TaskDto.builder()
                .id(id)
                .title("New title")
                .build();
        TaskDto taskDto2 = TaskDto.builder()
                .id(id)
                .description("New description")
                .build();
        TaskDto taskDto3 = TaskDto.builder()
                .id(id)
                .status(Status.DONE)
                .build();
        TaskDto taskDto4 = TaskDto.builder()
                .id(id)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(40, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .build();
        TaskDto taskDto5 = TaskDto.builder()
                .id(id)
                .priority(PriorityTask.STANDARD)
                .build();

        //Попытка обновить title
        Task newTitle = taskService.update(taskDto1);
        assertEquals(oldTask.getId(), newTitle.getId());
        assertEquals(oldTask.getUser_id(), newTitle.getUser_id());

        assertNotEquals(oldTask.getTitle(), newTitle.getTitle());
        assertEquals(taskDto1.getTitle(), newTitle.getTitle());

        assertEquals(oldTask.getDescription(), newTitle.getDescription());
        assertEquals(oldTask.getStatus(), newTitle.getStatus());
        assertEquals(oldTask.getPriority(), newTitle.getPriority());
        assertEquals(oldTask.getExpirationDate(), newTitle.getExpirationDate());

        //Попытка обновить Description
        Task newDescription = taskService.update(taskDto2);
        assertEquals(oldTask.getId(), newDescription.getId());
        assertEquals(oldTask.getUser_id(), newDescription.getUser_id());

        assertNotEquals(oldTask.getTitle(), newDescription.getTitle());
        assertEquals(taskDto1.getTitle(), newDescription.getTitle());

        assertNotEquals(oldTask.getDescription(), newDescription.getDescription());
        assertEquals(taskDto2.getDescription(), newDescription.getDescription());

        assertEquals(oldTask.getStatus(), newDescription.getStatus());
        assertEquals(oldTask.getPriority(), newDescription.getPriority());
        assertEquals(oldTask.getExpirationDate(), newDescription.getExpirationDate());


        //Попытка обновить Status
        Task newStatus = taskService.update(taskDto3);
        assertEquals(oldTask.getId(), newStatus.getId());
        assertEquals(oldTask.getUser_id(), newStatus.getUser_id());

        assertNotEquals(oldTask.getTitle(), newStatus.getTitle());
        assertEquals(taskDto1.getTitle(), newStatus.getTitle());

        assertNotEquals(oldTask.getDescription(), newStatus.getDescription());
        assertEquals(taskDto2.getDescription(), newStatus.getDescription());

        assertNotEquals(oldTask.getStatus(), newStatus.getStatus());
        assertEquals(taskDto3.getStatus(), newStatus.getStatus());

        assertEquals(oldTask.getPriority(), newStatus.getPriority());
        assertEquals(oldTask.getExpirationDate(), newStatus.getExpirationDate());

        //Попытка обновить ExpirationDate
        Task newExpirationDate = taskService.update(taskDto4);
        assertEquals(oldTask.getId(), newExpirationDate.getId());
        assertEquals(oldTask.getUser_id(), newExpirationDate.getUser_id());

        assertNotEquals(oldTask.getTitle(), newExpirationDate.getTitle());
        assertEquals(taskDto1.getTitle(), newExpirationDate.getTitle());

        assertNotEquals(oldTask.getDescription(), newExpirationDate.getDescription());
        assertEquals(taskDto2.getDescription(), newExpirationDate.getDescription());

        assertNotEquals(oldTask.getStatus(), newExpirationDate.getStatus());
        assertEquals(taskDto3.getStatus(), newExpirationDate.getStatus());

        assertNotEquals(oldTask.getExpirationDate(), newExpirationDate.getExpirationDate());
        assertEquals(taskDto4.getExpirationDate(),newExpirationDate.getExpirationDate().toString());

        assertEquals(oldTask.getPriority(), newExpirationDate.getPriority());

        //Попытка обновить Priority
        Task newPriority = taskService.update(taskDto5);
        assertEquals(oldTask.getId(), newPriority.getId());
        assertEquals(oldTask.getUser_id(), newPriority.getUser_id());

        assertNotEquals(oldTask.getTitle(), newPriority.getTitle());
        assertEquals(taskDto1.getTitle(), newPriority.getTitle());

        assertNotEquals(oldTask.getDescription(), newPriority.getDescription());
        assertEquals(taskDto2.getDescription(), newPriority.getDescription());

        assertNotEquals(oldTask.getStatus(), newPriority.getStatus());
        assertEquals(taskDto3.getStatus(), newPriority.getStatus());

        assertNotEquals(oldTask.getExpirationDate(), newPriority.getExpirationDate());
        assertEquals(taskDto4.getExpirationDate(),newPriority.getExpirationDate().toString());

        assertNotEquals(oldTask.getPriority(), newPriority.getPriority());
        assertEquals(taskDto5.getPriority(), newPriority.getPriority());

    }

    //Меняет Статус на Failed, потому что дата выполнения просрочена
    @Test
    void update_ShouldChangeStatusOnFailed_BecauseOverdueDueDate() {

        Long id = id_l();
        Long userId = id_l();
        Task oldTask = Task.builder()
                .id(id)
                .user_id(userId)
                .title("Old title")
                .description("Old description")
                .status(Status.TODO)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().minus(24, ChronoUnit.HOURS), ZoneId.systemDefault()))
                .priority(PriorityTask.HIGH)
                .build();


        TaskDto newTask = TaskDto.builder()
                .id(id)
                .title("New title")
                .description("New description")
                .status(Status.IN_PROGRESS)
                .priority(PriorityTask.STANDARD)
                .build();

        when(taskRepository.findById(eq(id))).thenReturn(Optional.of(copyTask(oldTask)));
        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(properties.getMax_length_title()).thenReturn(max_length_title);
        when(properties.getMax_length_description()).thenReturn(max_length_description);

        Task result = taskService.update(newTask);

        verify(taskRepository).update(any(Task.class));

        assertNotNull(result);
        assertEquals(oldTask.getId(), result.getId());
        assertEquals(oldTask.getUser_id(), result.getUser_id());
        assertNotEquals(oldTask.getTitle(), result.getTitle());
        assertNotEquals(oldTask.getDescription(), result.getDescription());
        assertNotEquals(oldTask.getStatus().toString(), result.getStatus().toString());
        assertNotEquals(oldTask.getPriority().toString(), result.getPriority().toString());
        assertEquals(oldTask.getExpirationDate(), result.getExpirationDate());
        assertEquals(Status.FAILED, result.getStatus());

    }


    //Создает новый Task по данным из TaskDto. Проверяет валидность данных. Сохраняет новый task в БД. БД выдает id(Task).
    @Test
    void create_ShouldSaveTaskBD() {


        Long userId = id_l();

        TaskDto newTask = TaskDto.builder()
                .title("New title")
                .description("New description")
                .status(Status.TODO)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(24, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .priority(PriorityTask.HIGH)
                .build();


        when(properties.getMax_length_title()).thenReturn(max_length_title);
        when(properties.getMax_length_description()).thenReturn(max_length_description);
        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        doAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(id_l());
            return null;
        }).when(taskRepository).create(any(Task.class));


        Task result = taskService.create(newTask, userId);

        verify(taskRepository).create(any(Task.class));
        assertNotNull(result);

        assertNotNull(result.getId());
        assertEquals(userId, result.getUser_id());
        assertEquals(newTask.getTitle(), result.getTitle());
        assertEquals(newTask.getDescription(), result.getDescription());
        assertEquals(newTask.getStatus().toString(), result.getStatus().toString());
        assertEquals(newTask.getPriority().toString(), result.getPriority().toString());
        assertEquals(newTask.getExpirationDate(), result.getExpirationDate().toString());

    }


    //Если данные не верны(userId равен null, данные не проходят валидацию, title равен null, время выполнение просрочено) выкидывает исключение ResourcesMappingException
    @Test
    void create_ShouldTrowsException() {

        StringBuilder st = new StringBuilder();
        for (int i = 0; i < max_length_title; i++) {
            st.append("title");
        }
        Long userId = id_l();

        TaskDto longTitle = TaskDto.builder()
                .title(st.toString())
                .description("New description")
                .status(Status.IN_PROGRESS)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .priority(PriorityTask.STANDARD)
                .build();


        TaskDto failedTitle = TaskDto.builder()
                .title("New ti ' <> []")
                .description("New description")
                .status(Status.IN_PROGRESS)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .priority(PriorityTask.STANDARD)
                .build();

        StringBuilder sta = new StringBuilder();
        for (int i = 0; i < max_length_description; i++) {
            sta.append("desc");
        }
        TaskDto longDescription = TaskDto.builder()
                .title("New title")
                .description(sta.toString())
                .status(Status.IN_PROGRESS)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .priority(PriorityTask.STANDARD)
                .build();


        TaskDto failedDiscription = TaskDto.builder()
                .title("New title")
                .description("New d<esc[ript]ion> ' ' '")
                .status(Status.IN_PROGRESS)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .priority(PriorityTask.STANDARD)
                .build();


        TaskDto nullTitle = TaskDto.builder()
                .title(null)
                .description("New description")
                .status(Status.IN_PROGRESS)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .priority(PriorityTask.STANDARD)
                .build();
        TaskDto failedExpiration = TaskDto.builder()
                .title("New title")
                .description("New description")
                .status(Status.IN_PROGRESS)
                .expirationDate(LocalDateTime.ofInstant(Instant.now().minus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString())
                .priority(PriorityTask.STANDARD)
                .build();


        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(properties.getMax_length_title()).thenReturn(max_length_title);
        when(properties.getMax_length_description()).thenReturn(max_length_description);

        assertThrows(ResourcesMappingException.class, () -> taskService.create(longTitle, userId));
        assertThrows(ResourcesMappingException.class, () -> taskService.create(longTitle, null));

        assertThrows(ResourcesMappingException.class, () -> taskService.create(failedTitle, userId));
        assertThrows(ResourcesMappingException.class, () -> taskService.create(longDescription, userId));
        assertThrows(ResourcesMappingException.class, () -> taskService.create(failedDiscription, userId));
        assertThrows(ResourcesMappingException.class, () -> taskService.create(nullTitle, userId));
        assertThrows(ResourcesMappingException.class, () -> taskService.create(failedExpiration, userId));



    }

    //Если не указан статус и приоритет, то они меняются на Status.TO DO и PriorityTask.STANDARD
    @Test
    void create_ShouldCompleteTheData() {

        Long userId = id_l();

        TaskDto newTask = new TaskDto();
        newTask.setTitle("New title");
        newTask.setDescription("New description");
        newTask.setStatus(null);
        newTask.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString());
        newTask.setPriority(null);
        doAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(id_l());
            return null;
        }).when(taskRepository).create(any(Task.class));
        when(pattern.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        when(properties.getMax_length_title()).thenReturn(max_length_title);
        when(properties.getMax_length_description()).thenReturn(max_length_description);
        Task result = taskService.create(newTask, userId);

        verify(taskRepository).create(any(Task.class));
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(userId, result.getUser_id());
        assertEquals(newTask.getTitle(), result.getTitle());
        assertEquals(newTask.getDescription(), result.getDescription());
        assertNotNull(result.getStatus());
        assertNotNull(result.getPriority());
        assertEquals(newTask.getExpirationDate(), result.getExpirationDate().toString());

    }

    @Test
    void isTaskOwner_ShouldCallRepository() {
        Long idTask = id_l();
        Long idUser = id_l();
        taskService.isTaskOwner(idUser, idTask);


        verify(taskRepository).isTaskOwner(eq(idUser), eq(idTask));
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        Long idUser = id_l();

        taskService.delete(idUser);

        verify(taskRepository).delete(eq(idUser));

    }




    private Task copyTask(Task task) {
        Task result = new Task();
        result.setId(task.getId());
        result.setUser_id(task.getUser_id());
        result.setTitle(task.getTitle());
        result.setDescription(task.getDescription());
        result.setStatus(task.getStatus());
        result.setExpirationDate(task.getExpirationDate());
        result.setPriority(task.getPriority());
        return result;

    }

    private Long id_l() {
        UUID uuid = UUID.randomUUID();
        Long idLong = Math.abs(uuid.getMostSignificantBits());
        return idLong;
    }


}