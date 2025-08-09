package ec.com.todo.apptasks.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BoardExceptionHandler {
    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<BoardErrorResponse> handleResourceNotFound(BoardNotFoundException ex) {
        BoardErrorResponse error = new BoardErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
