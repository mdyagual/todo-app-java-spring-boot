package ec.com.todo.apptasks.phase.dto.request;

import ec.com.todo.apptasks.phase.entity.PhaseName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CreatePhaseDTO {
    @NotNull(message = "Phase name is required: TO_DO, IN_PROGRESS, REVIEW, BLOCKED, DONE")
    private PhaseName name;
}
