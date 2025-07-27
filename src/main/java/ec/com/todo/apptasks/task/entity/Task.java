package ec.com.todo.apptasks.task.entity;

import ec.com.todo.apptasks.phase.entity.Phase;
import jakarta.persistence.*;
import lombok.*;

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
    public Long id;
    public String description;
    public String status;

    @ManyToOne
    @JoinColumn(name = "phase_id")
    private Phase phase;
}
