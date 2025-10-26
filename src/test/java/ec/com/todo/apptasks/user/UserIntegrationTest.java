package ec.com.todo.apptasks.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.todo.apptasks.user.dto.request.CreateUserDTO;
import ec.com.todo.apptasks.user.dto.request.DeleteUserDTO;
import ec.com.todo.apptasks.user.dto.response.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // rolls back DB changes after each test
class UserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void createUser_shouldReturn201() throws Exception {
        CreateUserDTO createUserDTO =
                new CreateUserDTO("Sarah", "dnyagual", "sarah@mail.com");

        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createUserDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Sarah"));
    }

    @Test
    void getAllUsers_shouldReturnOkAndList() throws Exception {
        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void deleteUser_shouldReturnAccepted() throws Exception {
        // First create a user
        CreateUserDTO createUserDTO =
                new CreateUserDTO("Test", "testuser", "test@mail.com");

        String response = mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createUserDTO)))
                .andReturn().getResponse().getContentAsString();

        UserDTO user = mapper.readValue(response, UserDTO.class);

        // Then delete it
        DeleteUserDTO deleteUserDTO = new DeleteUserDTO(user.getId());

        mockMvc.perform(post("/api/users/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteUserDTO)))
                .andExpect(status().isAccepted());
    }
}

