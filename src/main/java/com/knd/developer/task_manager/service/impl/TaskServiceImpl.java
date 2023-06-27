package com.knd.developer.task_manager.service.impl;

import com.knd.developer.task_manager.domain.exception.ResourceNotFoundException;
import com.knd.developer.task_manager.domain.exception.ResourcesMappingException;
import com.knd.developer.task_manager.domain.task.PriorityTask;
import com.knd.developer.task_manager.domain.task.Status;
import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.repository.TaskRepository;
import com.knd.developer.task_manager.service.TaskService;
import com.knd.developer.task_manager.service.props.PatternString;
import com.knd.developer.task_manager.service.props.TaskProperties;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final PatternString pattern;
    private final TaskProperties taskProperties;


    /**
     * Отправляет запрос в taskRepository
     * Если task по-данному id нет, выдает исключение ResourceNotFoundException
     *
     * @param id - id(Task)
     * @return - Task найденный в БД по id(Task)
     */
    @Override
    @Transactional(readOnly = true)
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));
    }

    /**
     * Делает запрос в TaskRepository и проверяет дедлайн тасков
     * Если у пользователя нет таксков, вернется пустой лист
     * Если дата дедлайна у таска закончилась, то статус таска поменяется на Failed
     *
     * @param id - id User для которого нужны таски
     * @return возвращат List<Task>, если у пользователя нет такс, возвращает пустой список
     */
    @Override
    public List<Task> getAllTasksByUserId(Long id) {
        List<Task> tasks = taskRepository.findAllByUserId(id);
        for (Task task : tasks) {
            if (resetStatus(task)) taskRepository.update(task);
        }
        return tasks;
    }


    /**
     * Проверяет новые данные на валидность, проверяет время выполнения task, сохраняет новые данные БД.
     * Меняет статус task на Failed, если время выполнения просрочено.
     * Можно обновлять данные по одному полю, если в TaskDto есть id(Task) и данные в поле, которое нужно обновить.
     * Если передать TaskDto с id(Task), которого нет в БД, то выбросит исключение ResourcesNotFoundException.
     * Если попробовать передать не валидные данные, то выбросит исключение ResourcesMappingException.
     * Если принимаемый TaskDto пустой(кроме id(Task)), то ни каких изменений в task не будет.
     *
     * @param task - TaskDto, обязательно должно быть id(Task)
     * @return -Task, из БД с обновленными данными
     */
    @Override
    @Transactional
    public Task update(TaskDto task) {
        Task oldTask = getById(task.getId());

        validationDateTask(oldTask, task);

        resetStatus(oldTask);
        taskRepository.update(oldTask);

        return oldTask;
    }

    /**
     * Создает новый Task по данным из TaskDto, проверяет валидность данных и сохраняет новый Task в БД.
     * БД генерирует id(Task)
     * Если данные не верны( userId == null, title == null,
     * данные из TaskDto не проходят валидацию(т.е. использованы не допустимые знаки или данные не допустимой длинны),
     * время выполнения просрочено) выкидывает исключение ResourcesMappingException.
     * Если не указаны статус и(или) приоритет, то они меняются на Status.ТODO и PriorityTask.STANDART
     *
     * @param taskDto - TaskDto, title не может быть null
     * @param userId  - id юзера, к которому добавляется task, не может быть null
     * @return - возвращает task с id(таска) который был получен при сохранении
     */
    @Override
    @Transactional
    public Task create(TaskDto taskDto, Long userId) {

        if (userId == null) {
            throw new ResourcesMappingException("User_Id не может быть null");
        }
        if (taskDto.getTitle() == null) {
            throw new ResourcesMappingException("title не может быть null");

        }
        if (taskDto.getExpirationDate() != null && LocalDateTime.parse(taskDto.getExpirationDate()).isBefore(LocalDateTime.now())) {
            throw new ResourcesMappingException("Не верное время завершение, время на выполнение уже закончилось");
        }
        Task task = Task.builder().user_id(userId).build();

        createValidationTask(task, taskDto);

        taskRepository.create(task);
        return task;
    }

    /**
     * Делает запрос в taskRepository - принадлежит ли данный таск, данному юзеру
     *
     * @param user_id - айди юзера
     * @param task_id - айди таска
     * @return - возврощает true, если принадлежит пользователю, иначе false
     */
    @Override
    public boolean isTaskOwner(Long user_id, Long task_id) {
        return taskRepository.isTaskOwner(user_id, task_id);
    }

    /**
     * Делает запрос в TaskRepository на удаление данного таска
     *
     * @param id - айди таска
     */
    @Override
    @Transactional
    public void delete(Long id) {
        taskRepository.delete(id);
    }

    private void createValidationTask(Task oldTask, TaskDto newTask) {

        if (newTask.getStatus() == null) {
            newTask.setStatus(Status.TODO);
        }
        if (newTask.getPriority() == null) {
            newTask.setPriority(PriorityTask.STANDARD);
        }

        validationDateTask(oldTask, newTask);
    }

    private void validationDateTask(Task oldTask, TaskDto newTask) {
        if (newTask.getTitle() != null) {
            if (newTask.getTitle().length() <= taskProperties.getMax_length_title()) {

                if (!pattern.getFORBIDDEN_JS_CHARS_PATTERN().matcher(newTask.getTitle()).find()) {
                    oldTask.setTitle(newTask.getTitle());
                } else {
                    throw new ResourcesMappingException("Не верный запрос изменения Таска, в title используются запрещеные символы");
                }
            } else {
                throw new ResourcesMappingException("Не верный запрос изменения Таска, title длинее 25 символов");
            }

        }
        if (newTask.getDescription() != null) {
            if (newTask.getDescription().length() < taskProperties.getMax_length_description()) {
                if (!pattern.getFORBIDDEN_JS_CHARS_PATTERN().matcher(newTask.getDescription()).find()) {
                    oldTask.setDescription(newTask.getDescription());
                } else {
                    throw new ResourcesMappingException("Не верный запрос изменения Таска, в description используются запрещеные символы");
                }
            } else {
                throw new ResourcesMappingException("Не верный запрос изменения Таска, description длинее  255 символов");
            }

        }

        if (newTask.getStatus() != null) {
            oldTask.setStatus(newTask.getStatus());

        }
        if (newTask.getPriority() != null) {
            oldTask.setPriority(newTask.getPriority());

        }
        if (newTask.getExpirationDate() != null) {
            if (LocalDateTime.parse(newTask.getExpirationDate()).isAfter(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()))) {
                oldTask.setExpirationDate(LocalDateTime.parse(newTask.getExpirationDate()));
            }

        }
    }


    /**
     * Проверяет время дедлайна
     * Если время существует и оно позже чем нынешнее время, меняет статус таска на FAILED
     *
     * @param task - таск для проверки
     */
    private boolean resetStatus(Task task) {
        if (task.getExpirationDate() != null) {

            if (task.getExpirationDate().isBefore(LocalDateTime.now())) {

                if (task.getStatus() != null) {

                    if (task.getStatus() == Status.TODO || task.getStatus() == Status.IN_PROGRESS) {
                        task.setStatus(Status.FAILED);
                        return true;
                    }
                }

            }
        }

        return false;
    }
}
