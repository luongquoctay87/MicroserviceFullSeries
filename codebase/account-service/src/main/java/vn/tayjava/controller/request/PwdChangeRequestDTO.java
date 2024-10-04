package vn.tayjava.controller.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class PwdChangeRequestDTO implements Serializable {
    @NotBlank(message = "id must be not blank")
    private Long id;

    @NotBlank(message = "oldPassword must be not blank")
    private String oldPassword;

    @NotBlank(message = "newPassword must be not blank")
    private String newPassword;
}
