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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    /*Testing preparation
     * 1. Elements that the service (in this case) needs to work
     * 2. Mock all of them except the service
     * 3. Set up the before each with the serviceImpl with all the elems that needs
     * */
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper mapper;

    @Mock
    private EntityManager entityManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp(){
        userService = new UserServiceImpl(
                userRepository,
                mapper,
                passwordEncoder,
                entityManager);

    }

    /* Testing steps:
        - 0. Input data (if needed, optional output data too)
        - 1. Stablish all mock behavior needed
        - 2. Call the method to test
        - 3. Verify the results
        - 4. Verify all interactions with mocks
    */

    //Success cases
    @Test
    void saveUserSuccess() {
        //0
        CreateUserDTO uDTO = new CreateUserDTO("mishell", "mdyagual", "mcym@gmail.com");
        User mappedUser = new User();
        mappedUser.setName(uDTO.getName());
        mappedUser.setUsername(uDTO.getUsername());
        mappedUser.setEmail(uDTO.getEmail());

        //1
        Mockito.when(userRepository.existsByUsernameOrEmail(uDTO.getUsername(), uDTO.getEmail()))
                .thenReturn(false);

        Mockito.when(mapper.toEntity(Mockito.any(CreateUserDTO.class)))
                .thenReturn(mappedUser);

        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("hashedPassword");


        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> {
                    User userToSave = invocation.getArgument(0);
                    userToSave.setId(1L); // Simulate that the user is saved and gets an ID
                    userToSave.setIsActive(true); //Simulate that the user is active when created bc it is in a PrePersist method
                    return userToSave;
                });
        Mockito.when(mapper.toDTO(Mockito.any(User.class)))
                .thenAnswer(invocation -> {
                    User savedUser = invocation.getArgument(0);
                    return new UserDTO(
                            savedUser.getId(),
                            savedUser.getName(),
                            savedUser.getUsername(),
                            savedUser.getEmail(),
                            savedUser.getPassword(),
                            savedUser.getIsActive(),
                            new ArrayList<>()
                    );
                });
        //2
        UserDTO result = userService.save(uDTO);

        //3
        assertAll("Saved user",
                () -> assertNotNull(result, "The result should not be null"),
                () -> assertEquals(1L, result.getId(), "The ID should be 1L"),
                () -> assertEquals(uDTO.getName(), result.getName(), "The name should match"),
                () -> assertEquals(uDTO.getUsername(), result.getUsername(), "The username should match"),
                () -> assertEquals(uDTO.getEmail(), result.getEmail(), "The email should match"),
                () -> assertTrue(result.getIsActive(), "The user should be active")
        );

        //4
        Mockito.verify(userRepository).existsByUsernameOrEmail(uDTO.getUsername(), uDTO.getEmail());
        Mockito.verify(mapper).toEntity(uDTO);
        Mockito.verify(passwordEncoder).encode(Mockito.anyString());
        Mockito.verify(userRepository).save(mappedUser);
    }

    @Test
    void getAllSuccess() {
        //0
        List<User> users = new ArrayList<>();
        users.add(new User(1L, "mishell", "mdyagual", "mcym@gmail.com", "hashedPassword", true, new ArrayList<>()));
        users.add(new User(1L, "dhara", "dyagual", "dnym@gmail.com", "hashedPassword", true, new ArrayList<>()));
        users.add(new User(1L, "erick", "eburgos", "eabl@gmail.com", "hashedPassword", true, new ArrayList<>()));

        //1
        Mockito.when(userRepository.findAll()).thenReturn(users);
        Mockito.when(mapper.toDTO(Mockito.any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    return new UserDTO(
                            user.getId(),
                            user.getName(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getIsActive(),
                            new ArrayList<>()
                    );
                });

        //2
        List<UserDTO> result = userService.getAll();

        //3
        assertAll("All users",
                () -> assertNotNull(result, "The result should not be null"),
                () -> assertEquals(3, result.size(), "There should be 3 users")
        );

        //4
        Mockito.verify(userRepository).findAll();
        Mockito.verify(mapper, Mockito.times(3)).toDTO(Mockito.any(User.class));


    }

    @Test
    void updateUserSuccess() {
        //0
        UpdateUserDTO nDTO = new UpdateUserDTO(1L, "mishell", "mdcyagual", "mcym@gmail.com");
        User existingUser = new User(1L, "mishell", "mdyagual", "mcym@gmail.com", "hashedPassword", true, new ArrayList<>());

        //1
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(existingUser));

        Mockito.doAnswer(invocation -> {
            User userToUpdate = invocation.getArgument(0);
            userToUpdate.setName(nDTO.getName());
            userToUpdate.setUsername(nDTO.getUsername());
            userToUpdate.setEmail(nDTO.getEmail());
            return null;
        }).when(mapper).updateEntity(Mockito.any(User.class), Mockito.any(UpdateUserDTO.class));

        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Mockito.when(mapper.toDTO(Mockito.any(User.class)))
                .thenAnswer(invocation -> {
                    User updatedUser = invocation.getArgument(0);
                    return new UserDTO(
                            updatedUser.getId(),
                            updatedUser.getName(),
                            updatedUser.getUsername(),
                            updatedUser.getEmail(),
                            updatedUser.getPassword(),
                            updatedUser.getIsActive(),
                            new ArrayList<>()
                    );
                });

        //2
        UserDTO result = userService.update(nDTO);
        //3
        assertAll("Updated user",
                () -> assertNotNull(result, "The result should not be null"),
                () -> assertEquals(nDTO.getId(), result.getId(), "The ID should match"),
                () -> assertEquals(nDTO.getName(), result.getName(), "The name should match"),
                () -> assertEquals(nDTO.getUsername(), result.getUsername(), "The username should match"),
                () -> assertEquals(nDTO.getEmail(), result.getEmail(), "The email should match")
        );

        //4
        Mockito.verify(userRepository).findById(nDTO.getId());
        Mockito.verify(mapper).updateEntity(existingUser, nDTO);
        Mockito.verify(userRepository).save(existingUser);
        Mockito.verify(mapper).toDTO(existingUser);


    }

    @Test
    void deleteUserSuccess() {
        //0
        DeleteUserDTO dDTO = new DeleteUserDTO(1L);
        User existingUser = new User(1L, "mishell", "mdyagual", "mcym@gmail.com", "hashedPassword", true, new ArrayList<>());

        //1
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        //2
        assertDoesNotThrow(() -> userService.delete(dDTO));
        //3
        assertFalse(existingUser.getIsActive(), "The user should be deactivated");
        //4
        Mockito.verify(userRepository).findById(dDTO.getId());
        Mockito.verify(userRepository).save(existingUser);
    }

    //Failure cases
    @Test
    void saveUserDuplicateFailure() {
        //0
        CreateUserDTO uDTO = new CreateUserDTO("mishell", "mdyagual", "mcym@gmail.com");

        //1
        Mockito.when(userRepository.existsByUsernameOrEmail(uDTO.getUsername(), uDTO.getEmail()))
                .thenReturn(true);

        //2
        Exception exception = assertThrows(DuplicateResourceException.class, () -> userService.save(uDTO));

        //3
        assertInstanceOf(DuplicateResourceException.class, exception);

        //4
        Mockito.verify(userRepository).existsByUsernameOrEmail(uDTO.getUsername(), uDTO.getEmail());
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoInteractions(mapper);
        Mockito.verifyNoInteractions(passwordEncoder);
    }

    @Test
    void updateUserNotFoundFailure() {
        //0
        UpdateUserDTO nDTO = new UpdateUserDTO(1L, "mishell", "mdcyagual", "mcym@gmail.com");

        //1
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());
        //2
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> userService.update(nDTO));

        //3
        assertInstanceOf(ResourceNotFoundException.class, exception);

        //4
        Mockito.verify(userRepository).findById(nDTO.getId());
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoInteractions(mapper);


    }

    @Test
    void deleteUserNotFoundFailure() {
        //0
        DeleteUserDTO dDTO = new DeleteUserDTO(1L);

        //1
        Mockito.when(userRepository.findById(Mockito.any(Long.class)))
                .thenReturn(Optional.empty());

        //2
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> userService.delete(dDTO));

        //3
        assertInstanceOf(ResourceNotFoundException.class, exception);

        //4
        Mockito.verify(userRepository).findById(dDTO.getId());
        Mockito.verifyNoMoreInteractions(userRepository);
        Mockito.verifyNoInteractions(mapper);

    }



}