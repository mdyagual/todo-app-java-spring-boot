package ec.com.todo.apptasks.user.entity;


import ec.com.todo.apptasks.board.entity.Board;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@Entity
@ToString(exclude = "boards")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1, initialValue = 1000)
    private Long id;
    private String name;
    private String username;
    private String email;
    private String password;
    private Boolean isActive;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards;

    @PrePersist
    protected void onCreate(){
        this.isActive = true;
    }
}
