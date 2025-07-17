package ec.com.todo.apptasks.board.entity;

import ec.com.todo.apptasks.phase.entity.Phase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Board {
    public Long id;
    public String title;
    public List<Phase> columns;
}
