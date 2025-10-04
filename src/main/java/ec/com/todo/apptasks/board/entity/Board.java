package ec.com.todo.apptasks.board.entity;

import ec.com.todo.apptasks.phase.entity.Phase;
import ec.com.todo.apptasks.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "boards")
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_seq")
    @SequenceGenerator(name = "board_seq", sequenceName = "board_sequence", allocationSize = 1, initialValue = 2000)
    private Long id;
    private String title;
    private LocalDate createdAt;
    private LocalDate lastModifiedAt;
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Phase> phases;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.lastModifiedAt = LocalDate.now();
        this.isActive = true;
    }
}
