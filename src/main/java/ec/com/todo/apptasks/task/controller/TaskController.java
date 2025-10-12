package ec.com.todo.apptasks.task.controller;

import ec.com.todo.apptasks.task.dto.request.CreateTaskDTO;
import ec.com.todo.apptasks.task.dto.request.DeleteTaskDTO;
import ec.com.todo.apptasks.task.dto.request.UpdateTaskDTO;
import ec.com.todo.apptasks.task.dto.response.TaskDTO;
import ec.com.todo.apptasks.task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody CreateTaskDTO taskDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.save(taskDTO));
    }

    @PostMapping("/update")
    public ResponseEntity<TaskDTO> updateTask(@Valid @RequestBody UpdateTaskDTO taskDTO) {
        return ResponseEntity.ok(taskService.update(taskDTO));
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteTask(@Valid @RequestBody DeleteTaskDTO taskDTO) {
        taskService.delete(taskDTO);
        return ResponseEntity.accepted().build();
    }
}

