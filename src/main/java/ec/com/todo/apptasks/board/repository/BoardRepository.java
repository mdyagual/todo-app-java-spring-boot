package ec.com.todo.apptasks.board.repository;

import ec.com.todo.apptasks.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    Boolean existsByTitleAndUserId(String title, Long userId);
}
