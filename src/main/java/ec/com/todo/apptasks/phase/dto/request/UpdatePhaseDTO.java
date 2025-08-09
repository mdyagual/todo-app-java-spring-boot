package ec.com.todo.apptasks.phase.dto.request;

import ec.com.todo.apptasks.phase.entity.PhaseName;
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
public class UpdatePhaseDTO {
    @NotNull(message = "ID must not be empty")
    private Long id;

    @NotNull(message = "Phase name is required: TO_DO, IN_PROGRESS, REVIEW, BLOCKED, DONE")
    private PhaseName name;
}
