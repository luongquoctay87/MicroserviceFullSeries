package vn.tayjava.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tayjava.controller.request.PwdChangeRequestDTO;
import vn.tayjava.controller.request.UserCreationRequestDTO;
import vn.tayjava.controller.request.UserUpdateDTO;
import vn.tayjava.controller.response.UserResponseDTO;
import vn.tayjava.exception.ResourceNotFoundException;
import vn.tayjava.service.AccountService;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final AccountService accountService;

    @GetMapping("/list")
    public List<UserResponseDTO> getAll() {
        return accountService.getUsers();
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponseDTO> getDetails(@PathVariable @Min(1) Long userId) {
        try {
            return new ResponseEntity<>(accountService.getUserDetails(userId), HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }

    @PostMapping("/add")
    public long addUser(@RequestBody @Valid UserCreationRequestDTO dto) {
        return accountService.addUser(dto);
    }

    @PutMapping("/upd")
    public void updateUser(@RequestBody UserUpdateDTO dto) {
        accountService.updateUser(dto);
    }

    @PatchMapping("/change-pwd")
    public void changePassword(@RequestBody PwdChangeRequestDTO dto) {
    }

    @DeleteMapping("/del/{userId}")
    public void deleteUser(@PathVariable @Min(1) Long userId) {

    }
}
