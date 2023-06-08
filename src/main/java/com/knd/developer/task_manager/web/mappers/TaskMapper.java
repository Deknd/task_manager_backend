package com.knd.developer.task_manager.web.mappers;

import com.knd.developer.task_manager.domain.task.Task;
import com.knd.developer.task_manager.web.dto.task.TaskDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);
    List<TaskDto> toDto(List<Task> tasks);
}
