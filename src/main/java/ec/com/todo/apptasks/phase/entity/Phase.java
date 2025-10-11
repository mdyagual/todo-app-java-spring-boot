package ec.com.todo.apptasks.phase.entity;

import ec.com.todo.apptasks.board.entity.Board;
import ec.com.todo.apptasks.task.entity.Task;
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
@Table(name = "phases")
@Entity
public class Phase {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "col_seq")
    @SequenceGenerator(name = "col_seq", sequenceName = "col_sequence", allocationSize = 1, initialValue = 3000)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PhaseName name;

    private LocalDate createdAt;
    private LocalDate lastModifiedAt;
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "phase", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Task> tasks;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDate.now();
        this.lastModifiedAt = LocalDate.now();
        this.isActive = false;
    }
}
