package vn.tayjava.controller.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class PwdChangeRequestDTO implements Serializable {
    private Long id;
    private String oldPassword;
    private String newPassword;
}
