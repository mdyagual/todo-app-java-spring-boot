package ec.com.todo.apptasks.task.service.impl;

import ec.com.todo.apptasks.phase.entity.Phase;
import ec.com.todo.apptasks.phase.service.PhaseService;
import ec.com.todo.apptasks.shared.exception.DuplicateResourceException;
import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
import ec.com.todo.apptasks.task.dto.request.CreateTaskDTO;
import ec.com.todo.apptasks.task.dto.request.DeleteTaskDTO;
import ec.com.todo.apptasks.task.dto.request.UpdateTaskDTO;
import ec.com.todo.apptasks.task.dto.response.TaskDTO;
import ec.com.todo.apptasks.task.entity.Task;
import ec.com.todo.apptasks.task.mapper.TaskMapper;
import ec.com.todo.apptasks.task.repository.TaskRepository;
import ec.com.todo.apptasks.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    /*Testing preparation
     * 1. Elements that the service (in this case) needs to work
     * 2. Mock all of them except the service
     * 3. Set up the before each with the serviceImpl with all the elems that needs
     * */
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper mapper;

    @Mock
    private PhaseService phaseService;

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskServiceImpl(taskRepository,
                mapper,
                phaseService);
    }

    /* Testing steps:
        - 0. Input data (if needed, optional output data too)
        - 1. Establish all mock behavior needed
        - 2. Call the method to test
        - 3. Verify the results
        - 4. Verify all interactions (the when's) with mocks
    */

    @Test
    void saveTaskSuccess() {
        //0
        CreateTaskDTO tDTO = new CreateTaskDTO("Description", 1L);
        Task mappedTask = new Task();
        mappedTask.setDescription(tDTO.getDescription());

        Phase phaseRef = new Phase();
        phaseRef.setId(tDTO.getPhaseId());

        //1
        Mockito.when(taskRepository.existsByDescriptionAndPhaseId(tDTO.getDescription(), tDTO.getPhaseId()))
                .thenReturn(false);

        Mockito.when(mapper.toEntity(tDTO))
                .thenReturn(mappedTask);

        Mockito.when(phaseService.getReferenceById(tDTO.getPhaseId()))
                .thenReturn(phaseRef);

        Mockito.when(taskRepository.save(Mockito.any(Task.class)))
                .thenAnswer(invocation -> {
                    Task taskToSave = invocation.getArgument(0);
                    taskToSave.setId(1L); // Simulate the database assigning an ID
                    taskToSave.setCreatedAt(LocalDate.now());
                    taskToSave.setLastModifiedAt(LocalDate.now());
                    taskToSave.setIsActive(true);
                    return taskToSave;
                });
        Mockito.when(mapper.toDTO(Mockito.any(Task.class)))
                .thenAnswer(invocation -> {
                    Task task = invocation.getArgument(0);
                    return new TaskDTO(task.getId(),
                            task.getDescription(),
                            task.getCreatedAt(),
                            task.getLastModifiedAt(),
                            task.getIsActive()
                    );
                });

        //2
        TaskDTO result = taskService.save(tDTO);

        //3
        assertAll( "Task created",
                () -> assertNotNull(result),
                () -> assertEquals(1L, result.getId()),
                () -> assertEquals(tDTO.getDescription(), result.getDescription()),
                () -> assertNotNull(result.getCreatedAt()),
                () -> assertNotNull(result.getLastModifiedAt()),
                () -> assertTrue(result.getIsActive())

        );

        //4
        Mockito.verify(taskRepository).existsByDescriptionAndPhaseId(tDTO.getDescription(), tDTO.getPhaseId());
        Mockito.verify(mapper).toEntity(tDTO);
        Mockito.verify(phaseService).getReferenceById(tDTO.getPhaseId());
        Mockito.verify(taskRepository).save(Mockito.any(Task.class));
        Mockito.verify(mapper).toDTO(Mockito.any(Task.class));
    }

    @Test
    void getAllSuccess() {
        //0
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "Task 1", LocalDate.now(), LocalDate.now(), true, new Phase()));
        tasks.add(new Task(2L, "Task 2", LocalDate.now(), LocalDate.now(), true, new Phase()));
        tasks.add(new Task(3L, "Task 3", LocalDate.now(), LocalDate.now(), true, new Phase()));

        //1
        Mockito.when(taskRepository.findAll())
                .thenReturn(tasks);
        Mockito.when(mapper.toDTO(Mockito.any(Task.class)))
                .thenAnswer(invocation -> {
                    Task task = invocation.getArgument(0);
                    return new TaskDTO(task.getId(),
                            task.getDescription(),
                            task.getCreatedAt(),
                            task.getLastModifiedAt(),
                            task.getIsActive()
                    );
                });
        //2
        List<TaskDTO> result = taskService.getAll();

        //3
        assertAll("All tasks",
                () -> assertNotNull(result),
                () -> assertEquals(3, result.size()),
                () -> assertEquals(1L, result.get(0).getId())
        );

        //4
        Mockito.verify(taskRepository).findAll();
        Mockito.verify(mapper, Mockito.times(3)).toDTO(Mockito.any(Task.class));
    }

    @Test
    void updateTaskSuccess() {
        //0
        UpdateTaskDTO tDTO = new UpdateTaskDTO(1L, "Updated Task");
        Task existingTask = new Task(1L, "Old Task", LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), true, new Phase());
        LocalDate previousLastModified = existingTask.getLastModifiedAt();

        //1
        Mockito.when(taskRepository.findById(tDTO.getId()))
                .thenReturn(java.util.Optional.of(existingTask));
        Mockito.doAnswer(invocation -> {
            Task taskToUpdate = invocation.getArgument(0);
            taskToUpdate.setDescription(tDTO.getDescription());
            taskToUpdate.setLastModifiedAt(LocalDate.now());
            return null;
        }).when(mapper).updateEntity(Mockito.any(Task.class), Mockito.any(UpdateTaskDTO.class));

        Mockito.when(taskRepository.save(Mockito.any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Mockito.when(mapper.toDTO(Mockito.any(Task.class)))
                .thenAnswer(invocation -> {
                    Task task = invocation.getArgument(0);
                    return new TaskDTO(task.getId(),
                            task.getDescription(),
                            task.getCreatedAt(),
                            task.getLastModifiedAt(),
                            task.getIsActive()
                    );
                });

        //2
        TaskDTO result = taskService.update(tDTO);
        
        //3
        assertAll("Updated Task",
                () -> assertNotNull(result),
                () -> assertEquals(tDTO.getId(), result.getId()),
                () -> assertEquals(tDTO.getDescription(), result.getDescription()),
                () -> assertEquals(existingTask.getCreatedAt(), result.getCreatedAt()),
                () -> assertNotEquals(previousLastModified, result.getLastModifiedAt()),
                () -> assertTrue(result.getIsActive())
        );

        //4
        Mockito.verify(taskRepository).findById(tDTO.getId());
        Mockito.verify(mapper).updateEntity(Mockito.any(Task.class), Mockito.any(UpdateTaskDTO.class));
        Mockito.verify(taskRepository).save(Mockito.any(Task.class));
        Mockito.verify(mapper).toDTO(Mockito.any(Task.class)); 
        
    }

    @Test
    void deleteTaskSuccess() {
        //0
        DeleteTaskDTO dDTO = new DeleteTaskDTO(1L);
        Task existingTask = new Task(1L, "Task to delete", LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), true, new Phase());
        
        //1
        Mockito.when(taskRepository.findById(Mockito.any(Long.class)))
                .thenReturn(java.util.Optional.of(existingTask));
        
        Mockito.when(taskRepository.save(Mockito.any(Task.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        //2
        assertDoesNotThrow(() -> taskService.delete(dDTO));
        
        //3
        assertFalse(existingTask.getIsActive(), "Task should be marked as inactive");
        
        //4
        Mockito.verify(taskRepository).findById(dDTO.getId());
        Mockito.verify(taskRepository).save(Mockito.any(Task.class));
        
    }
    
    @Test
    void saveDuplicateFailure(){
        //0
        CreateTaskDTO tDTO = new CreateTaskDTO("Description", 1L);

        //1
        Mockito.when(taskRepository.existsByDescriptionAndPhaseId(tDTO.getDescription(), tDTO.getPhaseId()))
                .thenReturn(true);

        //2
        Exception exception = assertThrows(DuplicateResourceException.class, () -> taskService.save(tDTO));

        //3
        assertInstanceOf(DuplicateResourceException.class, exception);

        //4
        Mockito.verify(taskRepository).existsByDescriptionAndPhaseId(tDTO.getDescription(), tDTO.getPhaseId());
        Mockito.verifyNoMoreInteractions(taskRepository);
        Mockito.verifyNoInteractions(mapper);
        Mockito.verifyNoInteractions(phaseService);
    }

    @Test
    void updateTaskNotFoundFailure(){
        //0
        UpdateTaskDTO tDTO = new UpdateTaskDTO(1L, "Updated Task");

        //1
        Mockito.when(taskRepository.findById(tDTO.getId()))
                .thenReturn(java.util.Optional.empty());

        //2
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> taskService.update(tDTO));

        //3
        assertInstanceOf(ResourceNotFoundException.class, exception);

        //4
        Mockito.verify(taskRepository).findById(tDTO.getId());
        Mockito.verifyNoMoreInteractions(taskRepository);
        Mockito.verifyNoInteractions(mapper);
        Mockito.verifyNoInteractions(phaseService);
    }

    @Test
    void deleteTaskNotFoundFailure(){
        //0
        DeleteTaskDTO dDTO = new DeleteTaskDTO(1L);

        //1
        Mockito.when(taskRepository.findById(Mockito.any(Long.class)))
                .thenReturn(java.util.Optional.empty());

        //2
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> taskService.delete(dDTO));

        //3
        assertInstanceOf(ResourceNotFoundException.class, exception);

        //4
        Mockito.verify(taskRepository).findById(dDTO.getId());
        Mockito.verifyNoMoreInteractions(taskRepository);
        Mockito.verifyNoInteractions(mapper);
        Mockito.verifyNoInteractions(phaseService);
    }

}