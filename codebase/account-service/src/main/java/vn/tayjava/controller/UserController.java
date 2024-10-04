package vn.tayjava.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j(topic = "USER-CONTROLLER")
public class UserController {

    private final AccountService accountService;

    @GetMapping("/list")
    public List<UserResponseDTO> getUserList(@RequestParam(defaultValue = "0", required = false) int page,
                                        @RequestParam(defaultValue = "20", required = false) int size,
                                        @RequestParam(required = false) String sort,
                                        @RequestParam(defaultValue = "") String... search) {
        log.info("-----[ getUserList ]-----");

        if (search.length > 0) {
            //accountService.advanceSearch(page, size, sort, search);
        }

        return accountService.getUsers(page, size, sort);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO getDetails(@PathVariable @Min(1) Long userId) {
        return accountService.getUserDetails(userId);
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
        accountService.changePassword(dto);
    }

    @DeleteMapping("/del/{userId}")
    public void deleteUser(@PathVariable @Min(1) Long userId) {
        accountService.deleteUser(userId);
    }
}
