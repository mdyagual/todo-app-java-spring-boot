package ec.com.todo.apptasks.task.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TaskErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public TaskErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
