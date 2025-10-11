package ec.com.todo.apptasks.task.service.impl;

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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final PhaseService phaseService;
    private final TaskMapper mapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper mapper, PhaseService phaseService) {
        this.taskRepository = taskRepository;
        this.phaseService = phaseService;
        this.mapper = mapper;
    }

    @Override
    public TaskDTO save(CreateTaskDTO tDTO) {
        if(taskRepository.existsByDescriptionAndPhaseId(tDTO.getDescription(), tDTO.getPhaseId())) {
            throw new DuplicateResourceException("Task", List.of("Description", tDTO.getPhaseId().toString()));
        }

        Task task = mapper.toEntity(tDTO);
        task.setPhase(phaseService.getReferenceById(tDTO.getPhaseId()));
        return mapper.toDTO(taskRepository.save(task));
    }

    @Override
    public List<TaskDTO> getAll() {
        return taskRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public TaskDTO update(UpdateTaskDTO tDTO) {
        return taskRepository.findById(tDTO.getId())
                .map(
                        task -> {
                            mapper.updateEntity(task, tDTO);
                            task.setLastModifiedAt(LocalDate.now());
                            return mapper.toDTO(taskRepository.save(task));
                        }).orElseThrow(() -> new ResourceNotFoundException("Task", tDTO.getId()));



    }

    @Override
    public void delete(DeleteTaskDTO tDTO) {
        taskRepository.findById(tDTO.getId())
                .ifPresentOrElse(
                        task -> {
                            task.setIsActive(false);
                            task.setLastModifiedAt(LocalDate.now());
                            taskRepository.save(task);
                        },
                        () -> {
                            throw new ResourceNotFoundException("Task", tDTO.getId());
                        }
                );

    }
}
