package ec.com.todo.apptasks.user.service.impl;

import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
import ec.com.todo.apptasks.user.dto.request.CreateUserDTO;
import ec.com.todo.apptasks.user.dto.request.DeleteUserDTO;
import ec.com.todo.apptasks.user.dto.request.UpdateUserDTO;
import ec.com.todo.apptasks.user.dto.response.UserDTO;
import ec.com.todo.apptasks.user.entity.User;
import ec.com.todo.apptasks.user.mapper.UserMapper;
import ec.com.todo.apptasks.user.mapper.UserMapperImpl;
import ec.com.todo.apptasks.user.repository.UserRepository;
import ec.com.todo.apptasks.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mapper = new UserMapperImpl();
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void save(CreateUserDTO uDTO) {
        // Generate random password (could be UUID or other logic)
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String hashedPassword = passwordEncoder.encode(rawPassword);

        User user = mapper.toEntity(uDTO);
        user.setPassword(hashedPassword); // Set the hashed password

        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getAll() {
       return userRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public void update(UpdateUserDTO uDTO) {
        userRepository.findById(uDTO.getId())
                .ifPresentOrElse(
                        user -> {
                            mapper.updateEntity(user, uDTO);
                            userRepository.save(user);
                        },
                        () -> {
                            throw new ResourceNotFoundException("User", uDTO.getId());
                        }
                );
    }

    @Override
    public void delete(DeleteUserDTO uDTO) {
        userRepository.findById(uDTO.getId())
                .ifPresentOrElse(
                        user -> {
                            user.setIsActive(false);
                            userRepository.save(user);
                        },
                        () -> {
                            throw new ResourceNotFoundException("User", uDTO.getId());
                        }
                );

    }
}
