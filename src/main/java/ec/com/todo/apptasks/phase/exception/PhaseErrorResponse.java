package ec.com.todo.apptasks.phase.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PhaseErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    public PhaseErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
