package ec.com.todo.apptasks.service.impl;

import ec.com.todo.apptasks.dto.UserDTO;
import ec.com.todo.apptasks.mapper.UserMapper;
import ec.com.todo.apptasks.repository.UserRepository;
import ec.com.todo.apptasks.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void save(UserDTO userDTO) {
        userRepository.save(UserMapper.toEntity(userDTO));
    }

    @Override
    public List<UserDTO> getAll() {
       return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();
    }


    @Override
    public UserDTO getById(Long id) {
        return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void update(UserDTO userDTO) {
        userRepository.save(UserMapper.toEntity(userDTO));
    }
}
