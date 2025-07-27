package ec.com.todo.apptasks.user.controller;

import ec.com.todo.apptasks.user.dto.request.CreateUserDTO;
import ec.com.todo.apptasks.user.dto.request.DeleteUserDTO;
import ec.com.todo.apptasks.user.dto.request.UpdateUserDTO;
import ec.com.todo.apptasks.user.dto.response.UserDTO;
import ec.com.todo.apptasks.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<UserDTO> getAll() {
        return userService.getAll();
    }

    @PostMapping("/create")
    public void create(CreateUserDTO userDTO) {
        userService.save(userDTO);
    }

    @PostMapping("/update")
    public void update(UpdateUserDTO userDTO) {
        userService.update(userDTO);
    }

    @PostMapping("/delete")
    public void delete(DeleteUserDTO userDTO) {
        userService.delete(userDTO);
    }


}
