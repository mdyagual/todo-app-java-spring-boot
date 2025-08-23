package ec.com.todo.apptasks.user.service;

import ec.com.todo.apptasks.user.dto.request.CreateUserDTO;
import ec.com.todo.apptasks.user.dto.request.DeleteUserDTO;
import ec.com.todo.apptasks.user.dto.request.UpdateUserDTO;
import ec.com.todo.apptasks.user.dto.response.UserDTO;
import ec.com.todo.apptasks.user.entity.User;

import java.util.List;

public interface UserService {
    UserDTO save(CreateUserDTO uDTO);
    List<UserDTO> getAll();
    void delete(DeleteUserDTO uDTO);
    UserDTO update(UpdateUserDTO uDTO);
    User getReferenceById(Long id);
    User getUserOrThrow(Long id);
}
