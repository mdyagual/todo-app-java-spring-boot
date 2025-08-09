package ec.com.todo.apptasks.user.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public UserErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();

    }

}
