package ec.com.todo.apptasks.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CreateTaskDTO {
    @Pattern(regexp = "^[a-zA-Z0-9 ]{3,20}$", message = "Description must be 4-50 characters, only letters and numbers")
    @NotBlank(message = "Description must not be empty")
    private String description;

    @NotNull(message = "ID must not be empty")
    private Long phaseId;
}
