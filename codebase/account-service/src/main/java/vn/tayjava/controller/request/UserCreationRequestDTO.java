package vn.tayjava.controller.request;

import lombok.*;
import vn.tayjava.common.Gender;
import vn.tayjava.common.UserType;

import java.io.Serializable;
import java.util.Date;

@Getter
public class UserCreationRequestDTO implements Serializable {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private Gender gender;
    private String phone;
    private String email;
    private String username;
    private String password;
    private UserType type;
    //private UserStatus status;
//    private Date createdAt;
//    private Date updatedAt;
}
