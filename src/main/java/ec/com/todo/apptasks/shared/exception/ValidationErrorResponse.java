package ec.com.todo.apptasks.shared.exception;

import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
public class ValidationErrorResponse {
    private int status;
    private Map<String, String> errors;
    private LocalDateTime timestamp;

    public ValidationErrorResponse(int status, Map<String, String> errors) {
        this.status = status;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }


}
