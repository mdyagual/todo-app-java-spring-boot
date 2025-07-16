package ec.com.todo.apptasks.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    public Long id;
    public String name;
    public String username;
    public String password;
    public Boolean isActive;


}
