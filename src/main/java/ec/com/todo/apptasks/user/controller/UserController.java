package ec.com.todo.apptasks.user.controller;

import ec.com.todo.apptasks.user.dto.request.CreateUserDTO;
import ec.com.todo.apptasks.user.dto.request.DeleteUserDTO;
import ec.com.todo.apptasks.user.dto.request.UpdateUserDTO;
import ec.com.todo.apptasks.user.dto.response.UserDTO;
import ec.com.todo.apptasks.user.service.UserService;
import jakarta.validation.Valid;
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
    public List<UserDTO> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping("/create")
    public void createUser(@Valid @RequestBody CreateUserDTO userDTO) {
        userService.save(userDTO);
    }

    @PostMapping("/update")
    public void updateUser(@Valid @RequestBody UpdateUserDTO userDTO) {
        userService.update(userDTO);
    }

    @PostMapping("/delete")
    public void deleteUser(@Valid @RequestBody DeleteUserDTO userDTO) {
        userService.delete(userDTO);
    }


}
