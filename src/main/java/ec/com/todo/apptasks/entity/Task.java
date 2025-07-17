package ec.com.todo.apptasks.entity;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    public Long id;
    public String description;
    public String status;
}
