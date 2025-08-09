package ec.com.todo.apptasks.task.service.impl;

import ec.com.todo.apptasks.shared.exception.ResourceNotFoundException;
import ec.com.todo.apptasks.task.dto.request.CreateTaskDTO;
import ec.com.todo.apptasks.task.dto.request.DeleteTaskDTO;
import ec.com.todo.apptasks.task.dto.request.UpdateTaskDTO;
import ec.com.todo.apptasks.task.dto.response.TaskDTO;
import ec.com.todo.apptasks.task.mapper.TaskMapper;
import ec.com.todo.apptasks.task.mapper.TaskMapperImpl;
import ec.com.todo.apptasks.task.repository.TaskRepository;
import ec.com.todo.apptasks.task.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper mapper;

    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
        this.mapper = new TaskMapperImpl();
    }

    @Override
    public void save(CreateTaskDTO tDTO) {
        taskRepository.save(mapper.toEntity(tDTO));
    }

    @Override
    public List<TaskDTO> getAll() {
        return taskRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public void update(UpdateTaskDTO tDTO) {
        taskRepository.findById(tDTO.getId())
                .ifPresentOrElse(
                        task -> {
                            mapper.updateEntity(task, tDTO);
                            task.setLastModifiedAt(LocalDate.now());
                            taskRepository.save(task);
                        },
                        () -> {
                            throw new ResourceNotFoundException("Task", tDTO.getId());
                        }
                );

    }

    @Override
    public void delete(DeleteTaskDTO tDTO) {
        taskRepository.findById(tDTO.getId())
                .ifPresentOrElse(
                        task -> {
                            task.setIsActive(false);
                            taskRepository.save(task);
                        },
                        () -> {
                            throw new ResourceNotFoundException("Task", tDTO.getId());
                        }
                );

    }
}
