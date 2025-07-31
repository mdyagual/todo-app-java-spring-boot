package ec.com.todo.apptasks.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UpdateUserDTO {
    public Long id;
    public String name;
    public String username;
    public String password;
    public Boolean isActive;
}
