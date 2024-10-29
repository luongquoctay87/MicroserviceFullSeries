package vn.tayjava.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class MobileSignUpRequest implements Serializable {

    @NotBlank(message = "{msg-validate-phone}")
    private String phone;

    @NotBlank(message = "{msg-validate-password}")
    private String password;

    @NotBlank(message = "deviceToken must be not blank")
    private String deviceToken;
}
