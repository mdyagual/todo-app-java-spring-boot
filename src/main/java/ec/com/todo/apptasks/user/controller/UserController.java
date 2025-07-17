package ec.com.todo.apptasks.user.controller;

import ec.com.todo.apptasks.user.dto.response.UserDTO;
import ec.com.todo.apptasks.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return userService.getAll();
    }
}
