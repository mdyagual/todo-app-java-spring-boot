package ec.com.todo.apptasks.task.entity;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    public Long id;
    public String description;
    public String status;
}
