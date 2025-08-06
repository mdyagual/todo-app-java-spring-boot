package ec.com.todo.apptasks.task.entity;

import ec.com.todo.apptasks.phase.entity.Phase;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tsk_seq")
    @SequenceGenerator(name = "tsk_seq", sequenceName = "tsk_sequence", allocationSize = 1, initialValue = 4000)
    private Long id;
    private String description;
    private LocalDate createdAt;
    private LocalDate lastModifiedAt;
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "phase_id")
    private Phase phase;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDate.now();
        this.lastModifiedAt = LocalDate.now();
        this.isActive = true;
    }
}
