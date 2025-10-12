package ec.com.todo.apptasks.task.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ec.com.todo.apptasks.shared.exception.DuplicateResourceException;
import ec.com.todo.apptasks.shared.exception.GlobalExceptionHandler;
import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
import ec.com.todo.apptasks.task.dto.request.CreateTaskDTO;
import ec.com.todo.apptasks.task.dto.request.DeleteTaskDTO;
import ec.com.todo.apptasks.task.dto.request.UpdateTaskDTO;
import ec.com.todo.apptasks.task.dto.response.TaskDTO;
import ec.com.todo.apptasks.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    private TaskController taskController;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        taskController = new TaskController(taskService);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).setControllerAdvice(new GlobalExceptionHandler()).build();
        mapper = new ObjectMapper();
    }

    //Success scenarios

    @Test
    void getAllTasks_shouldReturnContent() throws Exception {
        List<TaskDTO> tasks = new ArrayList<>();
        tasks.add(new TaskDTO(1L, "Task to complete", LocalDate.now(), LocalDate.now(), true));
        tasks.add(new TaskDTO(2L, "Task to complete 2", LocalDate.now(), LocalDate.now(), true));

        Mockito.when(taskService.getAll()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));

        Mockito.verify(taskService).getAll();

    }

    @Test
    void createTask_shouldSuccess() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO("Task to be done", 1L);
        TaskDTO taskDTO = new TaskDTO(2L, "Task to be done", LocalDate.now(), LocalDate.now(), true);

        Mockito.when(taskService.save(Mockito.any(CreateTaskDTO.class))).thenReturn(taskDTO);

        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value(createTaskDTO.getDescription()));

        Mockito.verify(taskService).save(Mockito.any(CreateTaskDTO.class));

    }

    @Test
    void updateTask_shouldSuccess() throws Exception {
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(1L, "New Task description");
        TaskDTO taskDTO = new TaskDTO(updateTaskDTO.getId(), updateTaskDTO.getDescription(), LocalDate.now(), LocalDate.now(),true);

        Mockito.when(taskService.update(Mockito.any(UpdateTaskDTO.class))).thenReturn(taskDTO);

        mockMvc.perform(post("/api/tasks/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateTaskDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(updateTaskDTO.getDescription()));

        Mockito.verify(taskService).update(Mockito.any(UpdateTaskDTO.class));

    }

    @Test
    void deleteTask_shouldSuccess() throws Exception{
        DeleteTaskDTO deleteTaskDTO = new DeleteTaskDTO(1L);

        Mockito.doNothing().when(taskService).delete(Mockito.any(DeleteTaskDTO.class));

        mockMvc.perform(post("/api/tasks/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteTaskDTO)))
                .andExpect(status().isAccepted());

        Mockito.verify(taskService).delete(Mockito.any(DeleteTaskDTO.class));
    }

    //Failure scenarios

    @Test
    void getTasks_shouldReturnEmpty() throws Exception {
        Mockito.when(taskService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/tasks/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));

        Mockito.verify(taskService).getAll();
    }

    @Test
    void saveTask_duplicatedFailure() throws Exception {
        CreateTaskDTO createTaskDTO = new CreateTaskDTO("Task to be done", 1L);

        Mockito.when(taskService.save(Mockito.any(CreateTaskDTO.class))).thenThrow(new DuplicateResourceException("Task", List.of(createTaskDTO.getDescription(), createTaskDTO.getPhaseId().toString())));

        mockMvc.perform(post("/api/tasks/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createTaskDTO)))
                .andExpect(status().isConflict());

        Mockito.verify(taskService).save(Mockito.any(CreateTaskDTO.class));
    }

    @Test
    void updateTask_NotFoundFailure() throws Exception {
        UpdateTaskDTO updateTaskDTO = new UpdateTaskDTO(1L, "New Task description");

        Mockito.when(taskService.update(Mockito.any(UpdateTaskDTO.class))).thenThrow(new ResourceNotFoundException("Task", updateTaskDTO.getId()));

        mockMvc.perform(post("/api/tasks/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateTaskDTO)))
                .andExpect(status().isNotFound());

        Mockito.verify(taskService).update(Mockito.any(UpdateTaskDTO.class));
    }

    @Test
    void deleteTask_NotFoundFailure() throws Exception{
        DeleteTaskDTO deleteTaskDTO = new DeleteTaskDTO(1L);

        Mockito.doThrow(new ResourceNotFoundException("Task", deleteTaskDTO.getId())).when(taskService).delete(Mockito.any(DeleteTaskDTO.class));

        mockMvc.perform(post("/api/tasks/delete")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(deleteTaskDTO)))
                .andExpect(status().isNotFound());

        Mockito.verify(taskService).delete(Mockito.any(DeleteTaskDTO.class));
    }
}