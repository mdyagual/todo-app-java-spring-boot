package ec.com.todo.apptasks.phase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.com.todo.apptasks.phase.entity.Phase;
import org.springframework.stereotype.Repository;

@Repository
public interface PhaseRepository extends JpaRepository<Phase, Long> {
}
