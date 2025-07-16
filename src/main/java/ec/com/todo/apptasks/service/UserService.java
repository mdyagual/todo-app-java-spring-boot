package ec.com.todo.apptasks.service;

import ec.com.todo.apptasks.dto.UserDTO;
import ec.com.todo.apptasks.entity.User;

import java.util.List;

public interface UserService {
    void save(UserDTO userDTO);
    List<UserDTO> getAll();
    UserDTO getById(Long id);
    void delete(Long id);
    void update(UserDTO userDTO);
}
