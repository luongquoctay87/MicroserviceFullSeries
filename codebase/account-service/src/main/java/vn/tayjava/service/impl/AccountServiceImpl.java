package vn.tayjava.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.tayjava.common.UserStatus;
import vn.tayjava.controller.request.PwdChangeRequestDTO;
import vn.tayjava.controller.request.UserCreationRequestDTO;
import vn.tayjava.controller.request.UserUpdateDTO;
import vn.tayjava.controller.response.UserResponseDTO;
import vn.tayjava.exception.ResourceNotFoundException;
import vn.tayjava.model.User;
import vn.tayjava.repository.UserRepository;
import vn.tayjava.service.AccountService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public long addUser(UserCreationRequestDTO dto) {
        log.info("-----[ addUser ]-----");

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGender(dto.getGender());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setType(dto.getType());
        user.setStatus(UserStatus.NONE);

        userRepository.save(user);

        // TODO send kafka to send email

        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateDTO dto) {
        log.info("-----[ updateUser ]-----");

        User user = getUser(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDateOfBirth(dto.getDateOfBirth());
        user.setGender(dto.getGender());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setType(dto.getType());

        userRepository.save(user);
    }

    @Override
    public void changePassword(PwdChangeRequestDTO dto) {
        log.info("-----[ changePassword ]-----");
    }

    @Override
    public void deleteUser(long userId) {
        log.info("-----[ deleteUser ]-----");
        User user = getUser(userId);
        user.setStatus(UserStatus.BLOCKED);

        userRepository.save(user);
    }

    @Override
    public UserResponseDTO getUserDetails(long userId) {
        log.info("-----[ getUserDetails ]-----");
        User user = getUser(userId);
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .email(user.getEmail())
                .phone(user.getPhone())
                .username(user.getUsername())
                .build();
    }

    @Override
    public List<UserResponseDTO> getUsers() {
        log.info("-----[ getUsers ]-----");
        List<User> userList = userRepository.findAll();
        return userList.stream().map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .dateOfBirth(user.getDateOfBirth())
                        .gender(user.getGender())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .username(user.getUsername())
                        .build())
                .toList();
    }

    private User getUser(long userId) {
        log.info("-----[ getUser ]-----");
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }
}
