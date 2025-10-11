package ec.com.todo.apptasks.user.controller;

import ec.com.todo.apptasks.user.dto.request.CreateUserDTO;
import ec.com.todo.apptasks.user.dto.request.DeleteUserDTO;
import ec.com.todo.apptasks.user.dto.request.UpdateUserDTO;
import ec.com.todo.apptasks.user.dto.response.UserDTO;
import ec.com.todo.apptasks.user.service.UserService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody CreateUserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(userDTO));
    }

    @PostMapping("/update")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UpdateUserDTO userDTO) {
        return ResponseEntity.ok(userService.update(userDTO));
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteUser(@Valid @RequestBody DeleteUserDTO userDTO) {
        userService.delete(userDTO);
        return ResponseEntity.accepted().build();
    }


}
