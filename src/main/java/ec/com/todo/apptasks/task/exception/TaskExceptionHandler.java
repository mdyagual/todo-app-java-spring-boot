package ec.com.todo.apptasks.task.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskExceptionHandler {
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<TaskErrorResponse> handleResourceNotFound(TaskNotFoundException ex) {
        TaskErrorResponse error = new TaskErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
