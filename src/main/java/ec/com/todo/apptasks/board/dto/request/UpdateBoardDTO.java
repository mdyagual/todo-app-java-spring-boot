package ec.com.todo.apptasks.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UpdateBoardDTO {
    @NotNull(message = "ID must not be empty")
    private Long id;

    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]{4,20}$", message = "Title must only contain letters and spaces")
    @NotBlank(message = "Title must not be empty")
    private String title;




}
