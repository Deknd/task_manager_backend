package com.knd.developer.task_manager.web.mappers;

import com.knd.developer.task_manager.domain.user.User;
import com.knd.developer.task_manager.web.dto.user.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserDto dto);
}
