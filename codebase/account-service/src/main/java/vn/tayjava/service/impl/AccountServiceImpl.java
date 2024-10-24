package vn.tayjava.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.tayjava.common.UserStatus;
import vn.tayjava.controller.request.PwdChangeRequestDTO;
import vn.tayjava.controller.request.UserCreationRequestDTO;
import vn.tayjava.controller.request.UserUpdateDTO;
import vn.tayjava.controller.response.UserResponseDTO;
import vn.tayjava.exception.InvalidDataException;
import vn.tayjava.exception.ResourceNotFoundException;
import vn.tayjava.model.User;
import vn.tayjava.repository.UserRepository;
import vn.tayjava.service.AccountService;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static vn.tayjava.common.Keyword.SORT_BY;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "ACCOUNT-SERVICE")
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String sendEmailTopic;

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

        if (user.getId() != null) {
            kafkaTemplate.send(sendEmailTopic, String.format("email=%s,id=%s,code=%s", user.getEmail(), user.getId(), "code@123"));
        }

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
        user.setType(dto.getType());

        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(PwdChangeRequestDTO dto) {
        log.info("-----[ changePassword ]-----");

        User user = getUser(dto.getId());
        boolean matches = passwordEncoder.matches(dto.getOldPassword(), user.getPassword());
        if (!matches) {
            throw new InvalidDataException("Password does not match");
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        userRepository.save(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
    public List<UserResponseDTO> getUsers(int page, int size, String sort) {
        log.info("-----[ getUsers ]-----");

        int number = 0;
        if (page > 0) {
            number = page - 1;
        }

        List<Sort.Order> orders = new ArrayList<>();

        if (StringUtils.hasLength(sort)) {
            // firstName:asc|desc
            Pattern pattern = Pattern.compile(SORT_BY.getValue());
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                if (matcher.group(3).equalsIgnoreCase("asc")) {
                    orders.add(new Sort.Order(Sort.Direction.ASC, matcher.group(1)));
                } else {
                    orders.add(new Sort.Order(Sort.Direction.DESC, matcher.group(1)));
                }
            }
        }

        Pageable pageable = PageRequest.of(number, size, Sort.by(orders));

        Page<User> users = userRepository.findAll(pageable);

        return users.stream().filter(
                        user -> user.getStatus() == UserStatus.ACTIVE
                )
                .map(user -> UserResponseDTO.builder()
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

    /**
     * Get user by ID
     *
     * @param userId
     * @return
     */
    private User getUser(long userId) {
        log.info("-----[ getUser ]-----");
        return userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
    }
}
