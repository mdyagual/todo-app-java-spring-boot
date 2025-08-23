package ec.com.todo.apptasks.user.service.impl;

import ec.com.todo.apptasks.shared.exception.DuplicateResourceException;
import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
import ec.com.todo.apptasks.user.dto.request.CreateUserDTO;
import ec.com.todo.apptasks.user.dto.request.DeleteUserDTO;
import ec.com.todo.apptasks.user.dto.request.UpdateUserDTO;
import ec.com.todo.apptasks.user.dto.response.UserDTO;
import ec.com.todo.apptasks.user.entity.User;
import ec.com.todo.apptasks.user.mapper.UserMapper;
import ec.com.todo.apptasks.user.repository.UserRepository;
import ec.com.todo.apptasks.user.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final EntityManager entityManager;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, UserMapper mapper, PasswordEncoder passwordEncoder, EntityManager entityManager) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.entityManager = entityManager;
    }

    public User getReferenceById(Long id) {
        return entityManager.getReference(User.class, id);
    }

    public User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    @Override
    public UserDTO save(CreateUserDTO uDTO) {

        if (userRepository.existsByUsernameOrEmail(uDTO.getUsername(), uDTO.getEmail())){
            throw new DuplicateResourceException("User", List.of(uDTO.getUsername(), uDTO.getEmail()));
        }

        // Generate random password (could be UUID or other logic)
        String rawPassword = UUID.randomUUID().toString().substring(0, 8);
        String hashedPassword = passwordEncoder.encode(rawPassword);

        User user = mapper.toEntity(uDTO);
        user.setPassword(hashedPassword); // Set the hashed password


        return mapper.toDTO(userRepository.save(user));
    }

    @Override
    public List<UserDTO> getAll() {
       return userRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public UserDTO update(UpdateUserDTO uDTO) {
        return userRepository.findById(uDTO.getId())
                .map(user -> {
                    mapper.updateEntity(user, uDTO);
                    return mapper.toDTO(userRepository.save(user));
                })
                .orElseThrow(() -> new ResourceNotFoundException("User", uDTO.getId()));
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
