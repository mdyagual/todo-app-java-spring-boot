package ec.com.todo.apptasks.entity;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public Long id;
    public String name;
    public String role;
    public Integer assignedTasks;

}
