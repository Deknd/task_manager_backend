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
import com.knd.developer.task_manager.web.dto.task.TaskUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;
    @SpyBean
    private TaskProperties taskProperties;

    @InjectMocks
    private TaskServiceImpl taskService;
    @Mock
    private PatternString patternString;

    @Test
    void getById_ShouldReturnTaskIfNotNullElseThrowException() {
        Long id = id_l();
        Task task = mock(Task.class);
        when(taskRepository.findById(eq(id))).thenReturn(Optional.of(task));

        Task result = taskService.getById(id);

        assertThrows(ResourceNotFoundException.class, () -> taskService.getById(12L));
        assertEquals(task, result);
    }

    @Test
    void getAllByUserId_ShouldReturnAllTaskGivenUserIfTaskNoReturnEmptyList() {

        Long id = id_l();
        Task task1 = mock(Task.class);
        Task task2 = mock(Task.class);
        Task task3 = mock(Task.class);
        Task task4 = mock(Task.class);
        List<Task> tasks = List.of(task1, task2, task3, task4);
        when(taskRepository.findAllByUserId(eq(id))).thenReturn(tasks);

        List<Task> result = taskService.getAllTasksByUserId(id);
        List<Task> listNull = taskService.getAllTasksByUserId(234L);

        assertEquals(tasks, result);
        assertTrue(listNull.isEmpty());

    }

    @Test
    void update_ShouldChangeTheOldTask() {
        final Pattern FORBIDDEN_JS_CHARS_PATTERN = Pattern.compile("[<>&\']");

        Long id = id_l();
        Long userId = id_l();
        Task oldTask = new Task();
        oldTask.setId(id);
        oldTask.setUser_id(userId);
        oldTask.setTitle("Old title");
        oldTask.setDescription("Old description");
        oldTask.setStatus(Status.TODO);
        oldTask.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(24, ChronoUnit.HOURS), ZoneId.systemDefault()));
        oldTask.setPriority(PriorityTask.HIGH);

        TaskUpdateDto newTask = new TaskUpdateDto();
        newTask.setId(id);
        newTask.setTitle("New title");
        newTask.setDescription("New description");
        newTask.setStatus(Status.IN_PROGRESS);
        newTask.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()));
        newTask.setPriority(PriorityTask.STANDARD);

        when(taskRepository.findById(eq(id))).thenReturn(Optional.of(copyTask(oldTask)));
        when(patternString.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);

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

    @Test
    void update_ShouldTrowsException() {
        final Pattern FORBIDDEN_JS_CHARS_PATTERN = Pattern.compile("[<>&\']");

        Long id = id_l();
        Long userId = id_l();
        Task oldTask = new Task();
        oldTask.setId(id);
        oldTask.setUser_id(userId);
        oldTask.setTitle("Old title");
        oldTask.setDescription("Old description");
        oldTask.setStatus(Status.TODO);
        oldTask.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(24, ChronoUnit.HOURS), ZoneId.systemDefault()));
        oldTask.setPriority(PriorityTask.HIGH);

        TaskUpdateDto newTask = new TaskUpdateDto();
        newTask.setId(id);
        newTask.setTitle("New titleasdfdsafsdafsadfsadfsadfsadfsadgadfasdasdfasdfsdcsd");
        newTask.setDescription("New description");
        newTask.setStatus(Status.IN_PROGRESS);
        newTask.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()));
        newTask.setPriority(PriorityTask.STANDARD);

        TaskUpdateDto newTask2 = new TaskUpdateDto();
        newTask2.setId(id);
        newTask2.setTitle("New ti ' <> []");
        newTask2.setDescription("New description");
        newTask2.setStatus(Status.IN_PROGRESS);
        newTask2.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()));
        newTask2.setPriority(PriorityTask.STANDARD);

        TaskUpdateDto newTask3 = new TaskUpdateDto();
        newTask3.setId(id);
        newTask3.setTitle("New titleas");
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < 255; i++) {
            st.append("New description ");
        }
        newTask3.setDescription(st.toString());
        newTask3.setStatus(Status.IN_PROGRESS);
        newTask3.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()));
        newTask3.setPriority(PriorityTask.STANDARD);

        TaskUpdateDto newTask4 = new TaskUpdateDto();
        newTask4.setId(id);
        newTask4.setTitle("New titleas");
        newTask4.setDescription("New d<esc[ript]ion> ' ' '");
        newTask4.setStatus(Status.IN_PROGRESS);
        newTask4.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()));
        newTask4.setPriority(PriorityTask.STANDARD);

        when(taskRepository.findById(eq(id))).thenReturn(Optional.of(copyTask(oldTask)));
        when(patternString.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);


        assertThrows(ResourcesMappingException.class, () -> taskService.update(newTask));
        assertThrows(ResourcesMappingException.class, () -> taskService.update(newTask2));
        assertThrows(ResourcesMappingException.class, () -> taskService.update(newTask3));
        assertThrows(ResourcesMappingException.class, () -> taskService.update(newTask4));


    }


    @Test
    void update_ShouldNoChangeOldTask() {
        final Pattern FORBIDDEN_JS_CHARS_PATTERN = Pattern.compile("[<>&\']");

        Long id = id_l();
        Long userId = id_l();
        Task oldTask = new Task();
        oldTask.setId(id);
        oldTask.setUser_id(userId);
        oldTask.setTitle("Old title");
        oldTask.setDescription("Old description");
        oldTask.setStatus(Status.TODO);
        oldTask.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(24, ChronoUnit.HOURS), ZoneId.systemDefault()));
        oldTask.setPriority(PriorityTask.HIGH);

        TaskUpdateDto newTask = new TaskUpdateDto();
        newTask.setId(id);

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

    @Test
    void create_ShouldSaveTaskInRepositoryAndReturnTaskWithId() {

        final Pattern FORBIDDEN_JS_CHARS_PATTERN = Pattern.compile("[<>&\']");

        Long userId = id_l();
        TaskDto newTask = new TaskDto();
        newTask.setTitle("New title");
        newTask.setDescription("New description");
        newTask.setStatus(Status.TODO);
        newTask.setExpirationDate((LocalDateTime.ofInstant(Instant.now().plus(24, ChronoUnit.HOURS), ZoneId.systemDefault())).toString());
        newTask.setPriority(PriorityTask.HIGH);


        when(patternString.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
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

    @Test
    void create_ShouldTrowsException() {
        final Pattern FORBIDDEN_JS_CHARS_PATTERN = Pattern.compile("[<>&\']");

        Long userId = id_l();

        TaskDto newTask = new TaskDto();
        newTask.setTitle("New titleasdfds afsdafsadfsad fsadfsadf sadgadfasda sdfasdf sdcsdrdsgsdfg djjjdjdrh");
        newTask.setDescription("New description");
        newTask.setStatus(Status.IN_PROGRESS);
        newTask.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString());
        newTask.setPriority(PriorityTask.STANDARD);

        TaskDto newTask2 = new TaskDto();
        newTask2.setTitle("New ti ' <> []");
        newTask2.setDescription("New description");
        newTask2.setStatus(Status.IN_PROGRESS);
        newTask2.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString());
        newTask2.setPriority(PriorityTask.STANDARD);

        TaskDto newTask3 = new TaskDto();
        newTask3.setTitle("New titleas");
        StringBuilder st = new StringBuilder();
        for (int i = 0; i < 255; i++) {
            st.append("New description ");
        }
        newTask3.setDescription(st.toString());
        newTask3.setStatus(Status.IN_PROGRESS);
        newTask3.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString());
        newTask3.setPriority(PriorityTask.STANDARD);

        TaskDto newTask4 = new TaskDto();
        newTask4.setTitle("New titleas");
        newTask4.setDescription("New d<esc[ript]ion> ' ' '");
        newTask4.setStatus(Status.IN_PROGRESS);
        newTask4.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString());
        newTask4.setPriority(PriorityTask.STANDARD);

        TaskDto newTask5 = new TaskDto();
        newTask5.setTitle(null);
        newTask5.setDescription("New description");
        newTask5.setStatus(Status.IN_PROGRESS);
        newTask5.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(12, ChronoUnit.HOURS), ZoneId.systemDefault()).toString());
        newTask5.setPriority(PriorityTask.STANDARD);

        when(patternString.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);


        assertThrows(ResourcesMappingException.class, () -> taskService.create(newTask, userId));
        assertThrows(ResourcesMappingException.class, () -> taskService.create(newTask, null));

        assertThrows(ResourcesMappingException.class, () -> taskService.create(newTask2, userId));
        assertThrows(ResourcesMappingException.class, () -> taskService.create(newTask3, userId));
        assertThrows(ResourcesMappingException.class, () -> taskService.create(newTask4, userId));
        assertThrows(ResourcesMappingException.class, () -> taskService.create(newTask5, userId));


    }

    @Test
    void create_ShouldCompleteTheData() {
        final Pattern FORBIDDEN_JS_CHARS_PATTERN = Pattern.compile("[<>&\']");

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
        when(patternString.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);

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

    @Test
    void create_ShouldChangeStatusDependingOnDateTime() {
        final Pattern FORBIDDEN_JS_CHARS_PATTERN = Pattern.compile("[<>&\']");

        Long userId = id_l();


        TaskDto newTask = new TaskDto();
        newTask.setTitle("New title");
        newTask.setDescription("New description");
        newTask.setStatus(Status.TODO);
        newTask.setExpirationDate(LocalDateTime.ofInstant(Instant.now().plus(24, ChronoUnit.HOURS), ZoneId.systemDefault()).toString());
        newTask.setPriority(PriorityTask.HIGH);

        TaskDto oldTask = new TaskDto();
        oldTask.setTitle("New title");
        oldTask.setDescription("New description");
        oldTask.setStatus(Status.TODO);
        oldTask.setPriority(PriorityTask.HIGH);


        TaskDto nullTask = new TaskDto();
        nullTask.setTitle("New title");
        nullTask.setDescription("New description");
        nullTask.setStatus(Status.TODO);
        nullTask.setExpirationDate(LocalDateTime.ofInstant(Instant.now().minus(24, ChronoUnit.HOURS), ZoneId.systemDefault()).toString());
        nullTask.setPriority(PriorityTask.HIGH);


        TaskDto failedTask = new TaskDto();
        failedTask.setTitle("New title");
        failedTask.setDescription("New description");
        failedTask.setStatus(Status.FAILED);
        failedTask.setExpirationDate(LocalDateTime.ofInstant(Instant.now().minus(24, ChronoUnit.HOURS), ZoneId.systemDefault()).toString());
        failedTask.setPriority(PriorityTask.HIGH);


        when(patternString.getFORBIDDEN_JS_CHARS_PATTERN()).thenReturn(FORBIDDEN_JS_CHARS_PATTERN);
        doAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(id_l());
            return null;
        }).when(taskRepository).create(any(Task.class));


        Task result1 = taskService.create(newTask, userId);
        Task result2 = taskService.create(oldTask, userId);
        Task result3 = taskService.create(nullTask, userId);
        Task result4 = taskService.create(failedTask, userId);


        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertNotNull(result4);


        assertEquals(newTask.getStatus(), result1.getStatus());
        assertEquals(oldTask.getStatus(), result2.getStatus());
        assertNotEquals(nullTask.getStatus(), result3.getStatus());
        assertEquals(failedTask.getStatus(), result4.getStatus());


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