package ec.com.todo.apptasks.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class DeleteUserDTO {
    @NotBlank(message = "ID must not be empty")
    public Long id;
}
