package ec.com.todo.apptasks.task.service;

import ec.com.todo.apptasks.task.dto.request.CreateTaskDTO;
import ec.com.todo.apptasks.task.dto.request.DeleteTaskDTO;
import ec.com.todo.apptasks.task.dto.request.UpdateTaskDTO;
import ec.com.todo.apptasks.task.dto.response.TaskDTO;

import java.util.List;

public interface TaskService {
    TaskDTO save(CreateTaskDTO taskDTO);
    List<TaskDTO> getAll();
    TaskDTO update(UpdateTaskDTO taskDTO);
    void delete(DeleteTaskDTO taskDTO);
}
