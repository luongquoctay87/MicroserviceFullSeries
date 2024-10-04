package vn.tayjava.service;

import vn.tayjava.controller.request.PwdChangeRequestDTO;
import vn.tayjava.controller.request.UserCreationRequestDTO;
import vn.tayjava.controller.request.UserUpdateDTO;
import vn.tayjava.controller.response.UserResponseDTO;

import java.util.List;

public interface AccountService {

    long addUser(UserCreationRequestDTO dto);

    void updateUser(UserUpdateDTO dto);

    void changePassword(PwdChangeRequestDTO dto);

    void deleteUser(long userId);

    UserResponseDTO getUserDetails(long userId);

    List<UserResponseDTO> getUsers(int page, int size, String sort);
}
