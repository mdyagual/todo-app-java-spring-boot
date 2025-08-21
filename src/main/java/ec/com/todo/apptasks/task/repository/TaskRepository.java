package ec.com.todo.apptasks.task.repository;

import ec.com.todo.apptasks.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Boolean existsByNameAndPhaseId(String name, Long phaseId);
}
