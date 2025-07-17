package ec.com.todo.apptasks.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateUserDTO {
    public String name;
    public String username;
    public String password;
}
