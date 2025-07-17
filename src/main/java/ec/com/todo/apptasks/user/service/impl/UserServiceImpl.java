package ec.com.todo.apptasks.user.service.impl;

import ec.com.todo.apptasks.user.dto.request.CreateUserDTO;
import ec.com.todo.apptasks.user.dto.request.UpdateUserDTO;
import ec.com.todo.apptasks.user.dto.response.UserDTO;
import ec.com.todo.apptasks.user.entity.User;
import ec.com.todo.apptasks.user.mapper.UserMapper;
import ec.com.todo.apptasks.user.mapper.UserMapperImpl;
import ec.com.todo.apptasks.user.repository.UserRepository;
import ec.com.todo.apptasks.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.mapper = new UserMapperImpl();
    }

    @Override
    public void save(CreateUserDTO uDTO) {
        userRepository.save(mapper.toEntity(uDTO));
    }

    @Override
    public List<UserDTO> getAll() {
       return userRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }


    @Override
    public UserDTO getById(Long id) {
        return userRepository.findById(id)
                .map(mapper::toDTO)
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void update(UpdateUserDTO uDTO) {
        User user = userRepository.findById(uDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + uDTO.getId()));
        mapper.updateEntity(user, uDTO);
        userRepository.save(user);
    }
}
