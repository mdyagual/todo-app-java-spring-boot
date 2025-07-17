package ec.com.todo.apptasks.mapper;

import ec.com.todo.apptasks.dto.UserDTO;
import ec.com.todo.apptasks.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserDTO userDTO);
    UserDTO toDTO(User user);
}
