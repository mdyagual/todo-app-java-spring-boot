package ec.com.todo.apptasks.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UpdateUserDTO {
    @NotBlank(message = "ID must not be empty")
    private Long id;
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]{2,20}$", message = "Name must only contain letters and spaces")
    @NotBlank(message = "Name must not be empty")
    private String name;
    @Pattern(regexp = "^[a-zA-Z0-9_]{4,20}$", message = "Username must be 4-20 characters, only letters, numbers, and underscores")
    @NotBlank(message = "Username must not be empty")
    private String username;
    @Pattern(regexp = "^[a-zA-Z0-9_!]{4,20}$", message = "Passwords must be 8-15 characters, only letters, numbers, underscores or exclamation mark")
    @NotBlank(message = "Password must not be empty")
    private String password;
    @NotBlank(message = "isActive must not be empty")
    private Boolean isActive;
}
