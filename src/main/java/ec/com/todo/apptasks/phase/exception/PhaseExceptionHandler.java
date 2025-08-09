package ec.com.todo.apptasks.phase.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PhaseExceptionHandler {
    @ExceptionHandler(PhaseNotFoundException.class)
    public ResponseEntity<PhaseErrorResponse> handleResourceNotFound(PhaseNotFoundException ex) {
        PhaseErrorResponse error = new PhaseErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
