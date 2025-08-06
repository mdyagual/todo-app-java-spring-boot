package ec.com.todo.apptasks.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class DeleteTaskDTO {
    @NotBlank(message = "ID must not be empty")
    private Long id;
}
