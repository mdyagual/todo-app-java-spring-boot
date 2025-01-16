package ec.com.todo.apptasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ec.com.todo.apptasks.entity.Vertical;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnRepository extends JpaRepository<Vertical, Long> {
}
