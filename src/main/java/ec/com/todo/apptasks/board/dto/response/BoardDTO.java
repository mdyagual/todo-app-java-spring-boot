package ec.com.todo.apptasks.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class BoardDTO {
    private Long id;
    private String title;
    private LocalDate createdAt;
    private LocalDate lastModifiedAt;
    private Boolean isActive;
    private List<Long> phases;
}
