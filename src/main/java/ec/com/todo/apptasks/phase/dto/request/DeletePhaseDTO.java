package ec.com.todo.apptasks.phase.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class DeletePhaseDTO {
    @NotNull(message = "ID must not be empty")
    private Long id;

}
