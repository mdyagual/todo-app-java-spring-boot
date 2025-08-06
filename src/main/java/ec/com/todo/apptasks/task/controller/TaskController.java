package ec.com.todo.apptasks.task.controller;

import ec.com.todo.apptasks.task.dto.request.CreateTaskDTO;
import ec.com.todo.apptasks.task.dto.request.DeleteTaskDTO;
import ec.com.todo.apptasks.task.dto.request.UpdateTaskDTO;
import ec.com.todo.apptasks.task.dto.response.TaskDTO;
import ec.com.todo.apptasks.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/all")
    public List<TaskDTO> getAllTasks() {
        return taskService.getAll();
    }

    @PostMapping("/create")
    public void createTask(@Valid @RequestBody CreateTaskDTO taskDTO) {
        taskService.save(taskDTO);
    }

    @PostMapping("/update")
    public void updateTask(@Valid @RequestBody UpdateTaskDTO taskDTO) {
        taskService.update(taskDTO);
    }

    @PostMapping("/delete")
    public void deleteTask(@Valid @RequestBody DeleteTaskDTO taskDTO) {
        taskService.delete(taskDTO);
    }
}

