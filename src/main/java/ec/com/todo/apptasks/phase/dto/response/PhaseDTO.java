package ec.com.todo.apptasks.phase.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PhaseDTO {
    private String name;
    private List<Long> tasks;
}
