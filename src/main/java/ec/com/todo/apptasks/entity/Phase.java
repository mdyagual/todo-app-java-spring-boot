package ec.com.todo.apptasks.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Phase {
    public Long id;
    public String title;
    public List<Task> tasks;
}
