package ec.com.todo.apptasks.user.mapper;

import ec.com.todo.apptasks.user.dto.request.CreateUserDTO;
import ec.com.todo.apptasks.user.dto.request.UpdateUserDTO;
import ec.com.todo.apptasks.user.dto.response.UserDTO;
import ec.com.todo.apptasks.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "boards", ignore = true)
    User toEntity(CreateUserDTO userDTO);

    @Mapping(target = "boards", ignore = true)
    UserDTO toDTO(User user);

    void updateEntity(@MappingTarget User user, UpdateUserDTO dto);
}
