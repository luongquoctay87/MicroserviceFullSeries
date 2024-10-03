package vn.tayjava.controller.response;

import lombok.*;
import vn.tayjava.common.Gender;
import vn.tayjava.common.UserType;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Gender gender;
    private String phone;
    private String email;
    private String username;
}
