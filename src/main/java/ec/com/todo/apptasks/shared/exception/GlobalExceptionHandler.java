package ec.com.todo.apptasks.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getBindingResult().getFieldErrors()
                    .stream()
                    .filter(err -> err.getDefaultMessage() != null)
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            err -> String.format("Field '%s': %s", err.getField(), err.getDefaultMessage()),
                            (msg1, msg2) -> msg1
                    )));

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
