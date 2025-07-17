package ec.com.todo.apptasks.user.service;

import ec.com.todo.apptasks.user.dto.response.UserDTO;

import java.util.List;

public interface UserService {
    void save(UserDTO userDTO);
    List<UserDTO> getAll();
    UserDTO getById(Long id);
    void delete(Long id);
    void update(UserDTO userDTO);
}
