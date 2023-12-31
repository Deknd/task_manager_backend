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
import java.time.format.DateTimeFormatter;
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
     * Делает запрос в TaskRepository и проверяет дедлайн task
     * Если у пользователя нет tasks, вернется пустой лист
     * Если дата дедлайна у task закончилась, то статус таска поменяется на Failed
     *
     * @param id - id User(Long)
     * @return возвращает List<Task>, если у пользователя нет task, возвращает пустой список
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
     * Если не указаны статус и(или) приоритет, то они меняются на Status.ТODO и PriorityTask.STANDARD
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");



        if (taskDto.getExpirationDate() != null) {
            if(taskDto.getExpirationDate().length()>17){
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
            }
            LocalDateTime timeTask = LocalDateTime.parse(taskDto.getExpirationDate(), formatter);
            if(timeTask.isBefore(LocalDateTime.now())){
                throw new ResourcesMappingException("Не верное время завершение, время на выполнение уже закончилось");

            }
        }
        Task task = Task.builder().user_id(userId).build();

        createValidationTask(task, taskDto);

        taskRepository.create(task);
        return task;
    }

    /**
     * Делает запрос в БД - принадлежит ли данный task(task_id), данному user(user_id)
     *
     * @param user_id - id(Long)
     * @param task_id - id(Long)
     * @return - возвращает true, если task принадлежит пользователю, иначе false
     */
    @Override
    public boolean isTaskOwner(Long user_id, Long task_id) {
        return taskRepository.isTaskOwner(user_id, task_id);
    }

    /**
     * Делает запрос в БД на удаление task по-указанному id.
     *
     * @param id  id(Task)
     */
    @Override
    @Transactional
    public void delete(Long id) {
        taskRepository.delete(id);
    }

    /**
     * Промежуточный метод. Добавляет в новый task(newTask) данные(Status.TO DO, PriorityTask.STANDARD) , если они равны null(Status == null, Priority == null).
     * @param oldTask task, который создается для отправки пользователю
     * @param newTask task, который приходит от пользователя, для дальнейшей валидации
     */
    private void createValidationTask(Task oldTask, TaskDto newTask) {

        if (newTask.getStatus() == null) {
            newTask.setStatus(Status.TODO);
        }
        if (newTask.getPriority() == null) {
            newTask.setPriority(PriorityTask.STANDARD);
        }

        validationDateTask(oldTask, newTask);
    }

    /**
     * Проверяет данные на валидность(newTask). Записывает данные в oldTask.
     *  "title"(newTask) - проверяется на длину( taskProperties.getMax_length_title() - указывается в файле application.yml(entity.task.max_length_title)),
     * проверяется на запрещенные символы ( pattern.getFORBIDDEN_JS_CHARS_PATTERN() - указывается в файле application.yml(entity.pattern.forbidden_js_chars_pattern)).
     *  "description"(newTask) - проверяется на длину( taskProperties.getMax_length_description() - указывается в файле application.yml(entity.task.max_length_description)),
     *      проверяется на запрещенные символы ( pattern.getFORBIDDEN_JS_CHARS_PATTERN() - указывается в файле application.yml(entity.pattern.forbidden_js_chars_pattern)).
     *  "expirationDate"(newTask) - не должен быть раньше настоящего времени.
     *
     * @param oldTask собранный task, из newTask при успешной валидации данных
     * @param newTask полученный от пользователя task, для прохождения валидации
     */
    private void validationDateTask(Task oldTask, TaskDto newTask) {
        if (newTask.getTitle() != null) {
            if (newTask.getTitle().length() <= taskProperties.getMax_length_title()) {

                if (!pattern.getFORBIDDEN_JS_CHARS_PATTERN().matcher(newTask.getTitle()).find()) {
                    oldTask.setTitle(newTask.getTitle());
                } else {
                    throw new ResourcesMappingException("Не верный запрос изменения Таска, в title используются запрещенные символы");
                }
            } else {
                throw new ResourcesMappingException("Не верный запрос изменения Таска, title длиннее 25 символов");
            }

        }
        if (newTask.getDescription() != null) {
            if (newTask.getDescription().length() < taskProperties.getMax_length_description()) {
                if (!pattern.getFORBIDDEN_JS_CHARS_PATTERN().matcher(newTask.getDescription()).find()) {
                    oldTask.setDescription(newTask.getDescription());
                } else {
                    throw new ResourcesMappingException("Не верный запрос изменения Таска, в description используются запрещенные символы");
                }
            } else {
                throw new ResourcesMappingException("Не верный запрос изменения Таска, description длиннее  255 символов");
            }

        }

        if (newTask.getStatus() != null) {
            oldTask.setStatus(newTask.getStatus());

        }
        if (newTask.getPriority() != null) {
            oldTask.setPriority(newTask.getPriority());

        }
        if (newTask.getExpirationDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            if(newTask.getExpirationDate().length()>16){
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
            }
            LocalDateTime timeTask = LocalDateTime.parse(newTask.getExpirationDate(),formatter);
            if (timeTask.isAfter(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()))) {
                oldTask.setExpirationDate(timeTask);
            }

        }
    }


    /**
     * Проверяет срок выполнения задания
     * Если время существует и оно раньше чем нынешнее время, меняет статус task на FAILED
     *
     * @param task - task для проверки
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
