package ec.com.todo.apptasks.board.exception;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
public class BoardErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;

    public BoardErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}
