package ec.com.todo.apptasks.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.todo.apptasks.shared.exception.DuplicateResourceException;
import ec.com.todo.apptasks.shared.exception.GlobalExceptionHandler;
import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
import ec.com.todo.apptasks.user.dto.request.CreateUserDTO;
import ec.com.todo.apptasks.user.dto.request.DeleteUserDTO;
import ec.com.todo.apptasks.user.dto.request.UpdateUserDTO;
import ec.com.todo.apptasks.user.dto.response.UserDTO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import ec.com.todo.apptasks.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    private UserController userController;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp(){
        userController = new UserController(userService);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).setControllerAdvice(new GlobalExceptionHandler()).build();
        mapper = new ObjectMapper();
    }


    //Success scenarios
    @Test
    void getAllUsers_shouldReturnContent() throws Exception {
        //0.
        List<UserDTO> users = new ArrayList<>();
        users.add(new UserDTO(1L, "Mishell","mdyagual","mishell@mail.com", "password", true, new ArrayList<>()));
        users.add(new UserDTO(2L, "Dhara","dnyagual","dhara@mail.com", "password", true, new ArrayList<>()));

        //1.
        Mockito.when(userService.getAll()).thenReturn(users);

        //2. y //3.
        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));

        //4.
        Mockito.verify(userService).getAll();

    }

    @Test
    void createUser_shouldSuccess() throws Exception {
        //0.
        CreateUserDTO createUserDTO = new CreateUserDTO("Mishell", "mdyagual", "mishell@mail.com");
        UserDTO userDTO = new UserDTO(1L, "Mishell","mdyagual","mishell@mail.com", "password", true, new ArrayList<>());

        //1.
        Mockito.when(userService.save(Mockito.any(CreateUserDTO.class))).thenReturn(userDTO);

        //2. y //3.
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(createUserDTO.getName()));

        //4.
        Mockito.verify(userService).save(Mockito.any(CreateUserDTO.class));
    }

    @Test
    void updateUser_shouldSuccess() throws Exception {
        //0.
        UpdateUserDTO updateUserDTO = new UpdateUserDTO(1L,"Mishell", "mdyagualmend", "mishell@mail.com");
        UserDTO userDTO = new UserDTO(1L,"Mishell", "mdyagualmend", "mishell@mail.com", "password", true, new ArrayList<>());

        //1.
        Mockito.when(userService.update(Mockito.any(UpdateUserDTO.class))).thenReturn(userDTO);

        //2. y //3.
        mockMvc.perform(post("/api/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(updateUserDTO.getUsername()));

        //4.
        Mockito.verify(userService).update(Mockito.any(UpdateUserDTO.class));
    }

    @Test
    void deleteUser_shouldSuccess() throws Exception {
        //0.
        DeleteUserDTO deleteUserDTO = new DeleteUserDTO(1L);

        //1.
        Mockito.doNothing().when(userService).delete(Mockito.any(DeleteUserDTO.class));

        //2. y //3.
        mockMvc.perform(post("/api/users/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteUserDTO)))
                .andExpect(status().isAccepted());

        //4.
        Mockito.verify(userService).delete(Mockito.any(DeleteUserDTO.class));


    }

    //Failure scenarios
    @Test
    void getUsers_ShouldReturnEmpty() throws Exception{
        Mockito.when(userService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        Mockito.verify(userService).getAll();
    }

    @Test
    void saveUser_duplicatedFailure() throws Exception {
        //0.
        CreateUserDTO createUserDTO = new CreateUserDTO("Mishell", "mdyagual", "mishell@mail.com");

        //1.
        Mockito.when(userService.save(Mockito.any(CreateUserDTO.class))).thenThrow(new DuplicateResourceException("User", List.of(createUserDTO.getUsername(), createUserDTO.getEmail())));

        //2. y //3.
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isConflict());

        //4.
        Mockito.verify(userService).save(Mockito.any(CreateUserDTO.class));
    }

    @Test
    void updateUser_NotFoundFailure() throws Exception{
        //0.
        UpdateUserDTO updateUserDTO = new UpdateUserDTO(1L,"Mishell", "mdyagual", "mishell@mail.com");

        //1.
        Mockito.when(userService.update(Mockito.any(UpdateUserDTO.class))).thenThrow(new ResourceNotFoundException("User", updateUserDTO.getId()));

        //2. y //3.
        mockMvc.perform(post("/api/users/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isNotFound());

        //4.
        Mockito.verify(userService).update(Mockito.any(UpdateUserDTO.class));

    }

    @Test
    void deleteUser_NotFoundFailure() throws Exception{
        //0.
        DeleteUserDTO deleteUserDTO = new DeleteUserDTO(1L);

        //1.
        Mockito.doThrow(new ResourceNotFoundException("User", deleteUserDTO.getId())).when(userService).delete(Mockito.any(DeleteUserDTO.class));

        //2. y //3.
        mockMvc.perform(post("/api/users/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteUserDTO)))
                .andExpect(status().isNotFound());

        //4.
        Mockito.verify(userService).delete(Mockito.any(DeleteUserDTO.class));

    }
}