package ec.com.todo.apptasks.mapper;

import ec.com.todo.apptasks.dto.UserDTO;
import ec.com.todo.apptasks.entity.User;

public class UserMapper {
    public static UserDTO toDTO(User user){
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getPassword(),
                user.getIsActive());
    }

    public static User toEntity(UserDTO userDTO){
        return new User(
                userDTO.getId(),
                userDTO.getName(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                userDTO.getIsActive());
    }
}
