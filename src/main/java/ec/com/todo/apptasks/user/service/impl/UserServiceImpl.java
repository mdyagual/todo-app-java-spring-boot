package ec.com.todo.apptasks.user.service.impl;

import ec.com.todo.apptasks.user.dto.response.UserDTO;
import ec.com.todo.apptasks.user.repository.UserRepository;
import ec.com.todo.apptasks.user.service.UserService;
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
        //userRepository.save(UserMapper.toEntity(userDTO));
    }

    @Override
    public List<UserDTO> getAll() {
        return null;
       /*return userRepository.findAll()
                .stream()
                .map(UserMapper::toDTO)
                .toList();*/
    }


    @Override
    public UserDTO getById(Long id) {
        return null;
        /*return userRepository.findById(id)
                .map(UserMapper::toDTO)
                .orElse(null);*/
    }

    @Override
    public void delete(Long id) {
        //userRepository.deleteById(id);
    }

    @Override
    public void update(UserDTO userDTO) {
        //userRepository.save(UserMapper.toEntity(userDTO));
    }
}
