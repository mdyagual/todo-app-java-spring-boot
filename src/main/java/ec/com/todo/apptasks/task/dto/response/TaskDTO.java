package ec.com.todo.apptasks.task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class TaskDTO {
    private Long id;
    private String description;
    private LocalDate createdAt;
    private LocalDate lastModifiedAt;
    private Boolean isActive;
}
