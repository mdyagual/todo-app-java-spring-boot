package ec.com.todo.apptasks.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class CreateUserDTO {
    public String name;
    public String username;
    public String email;
    public String password;

}
