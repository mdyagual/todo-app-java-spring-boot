package ec.com.todo.apptasks.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Table(name = "users")
@Entity
public class User {
    private Long id;
    private String name;
    private String username;
    private String password;
    private Boolean isActive;

    public User(String name, String username, String password, Boolean isActive) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.isActive = isActive;

    }
}
