package ec.com.todo.apptasks.shared.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import ec.com.todo.apptasks.board.exception.NumberOfPhasesException;
import ec.com.todo.apptasks.phase.entity.PhaseName;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //Field format is not valid
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

    //Enum value is not valid
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ValidationErrorResponse> handleEnumErrors(HttpMessageNotReadableException ex) {
        ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                Optional.ofNullable(ex.getCause())
                        .filter(InvalidFormatException.class::isInstance)
                        .map(InvalidFormatException.class::cast)
                        .map(e -> {
                            String wrongValue = e.getValue().toString();
                            String allowed = Arrays.stream(e.getTargetType().getEnumConstants())
                                    .filter(PhaseName.class::isInstance)
                                    .map(op -> ((PhaseName) op).getName())
                                    .collect(Collectors.joining(", "));
                            return Map.of("description",
                                    String.format("Invalid value: %s. Allowed values are [%s]", wrongValue, allowed));
                        })
                        .orElse(Map.of("description", "Invalid request body format"))
        );

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NumberOfPhasesException.class)
    public ResponseEntity<ErrorResponse> handleNumberOfPhases(NumberOfPhasesException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //Resource not found at service layer
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
    //Resource duplicated at service layer
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }
}
