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
import com.knd.developer.task_manager.web.dto.task.TaskUpdateDto;
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
     * Возвращает Таск по ID
     *
     * @param id - id таска, выданное при сохранении его в БД
     * @return - возвращает полный таск
     */
    @Override
    @Transactional(readOnly = true)
    public Task getById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));
    }

    /**
     * Возвращает все такски принадлежащие пользователю
     *
     * @param id - id User для которого нужны таски
     * @return возврощат List<Task>, если у пользователя нет такс, возврощает пустой список
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllTasksByUserId(Long id) {
        return taskRepository.findAllByUserId(id);
    }

    /**
     * Обновляет существующий таск
     *
     * @param task - таск для обновления
     * @return -обновленный Task
     */
    @Override
    @Transactional
    public Task update(TaskUpdateDto task) {
        Task oldTask = getById(task.getId());

        if (task.getTitle() != null) {
            if (!oldTask.getTitle().equals(task.getTitle())) {
                if (task.getTitle().length() <= taskProperties.getMax_length_title()) {

                    if (!pattern.getFORBIDDEN_JS_CHARS_PATTERN().matcher(task.getTitle()).find()) {
                        oldTask.setTitle(task.getTitle());
                    } else {
                        throw new ResourcesMappingException("Не верный запрос изменения Таска, в title используются запрещеные символы");
                    }
                } else {
                    throw new ResourcesMappingException("Не верный запрос изменения Таска, title длинее 25 символов");
                }
            }
        }
        if (task.getDescription() != null) {
            if (!oldTask.getDescription().equals(task.getDescription())) {
                if (task.getDescription().length() < taskProperties.getMax_length_description()) {
                    if (!pattern.getFORBIDDEN_JS_CHARS_PATTERN().matcher(task.getDescription()).find()) {
                        oldTask.setDescription(task.getDescription());
                    } else {
                        throw new ResourcesMappingException("Не верный запрос изменения Таска, в description используются запрещеные символы");
                    }
                } else {
                    throw new ResourcesMappingException("Не верный запрос изменения Таска, description длинее  255 символов");
                }
            }
        }

        if (task.getStatus() != null) {
            if (!oldTask.getStatus().equals(task.getStatus())) {
                oldTask.setStatus(task.getStatus());
            }
        }
        if (task.getPriority() != null) {
            if (!oldTask.getPriority().equals(task.getPriority())) {
                oldTask.setPriority(task.getPriority());
            }
        }
        if (task.getExpirationDate() != null) {
            if (!oldTask.getExpirationDate().equals(task.getExpirationDate())) {
                if (task.getExpirationDate().isAfter(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()))) {
                    oldTask.setExpirationDate(task.getExpirationDate());
                }
            }
        }

        resetStatus(oldTask);
        taskRepository.update(oldTask);
        return oldTask;
    }

    /**
     * Сохраняет полученый таск от клиента в репозитории
     *
     * @param taskDto - полученный от клиента таск
     * @param userId  - id юзера, за которым нужно закрепить таск
     * @return - возврощает таск с id(таска) который был получен при сохранении
     */
    @Override
    @Transactional
    public Task create(TaskDto taskDto, Long userId) {

        Task task = new Task();
        task.setUser_id(userId);
        if (taskDto.getTitle().length() <= taskProperties.getMax_length_title()) {
            if (!pattern.getFORBIDDEN_JS_CHARS_PATTERN().matcher(taskDto.getTitle()).find()) {
                task.setTitle(taskDto.getTitle());

            } else {
                throw new ResourcesMappingException("Не верный запрос создания Таска, title использованы не допустимые символы");

            }
        } else {
            throw new ResourcesMappingException("Не верный запрос создания Таска, title не может быть длинее 25 символов");

        }

        if (taskDto.getDescription() != null) {
            if (taskDto.getDescription().length() <= taskProperties.getMax_length_description()) {
                if (!pattern.getFORBIDDEN_JS_CHARS_PATTERN().matcher(taskDto.getDescription()).find()) {
                    task.setDescription(taskDto.getDescription());

                } else {
                    throw new ResourcesMappingException("Не верный запрос создания Таска, description использованы не допустимые символы");

                }
            } else {
                throw new ResourcesMappingException("Не верный запрос создания Таска, description не может быть больше 255");

            }
        }
        if (taskDto.getStatus() != null) {
            task.setStatus(taskDto.getStatus());
        } else {
            task.setStatus(Status.TODO);
        }
        if (taskDto.getPriority() != null) {
            task.setPriority(taskDto.getPriority());
        } else task.setPriority(PriorityTask.STANDARD);

        if (taskDto.getExpirationDate() != null) {
            task.setExpirationDate(LocalDateTime.parse(taskDto.getExpirationDate()));
        }

        resetStatus(task);
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


    /**
     * Проверяет время дедлайна, если время существует и оно позже чем нынешнее время, меняет статус таска на FAILED
     *
     * @param task - таск для проверки
     */
    private void resetStatus(Task task) {
        if (task.getExpirationDate() != null) {
            if (task.getExpirationDate().isBefore(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()))) {
                if (task.getStatus() != Status.FAILED) {
                    task.setStatus(Status.FAILED);
                }
            }
        }

    }
}
