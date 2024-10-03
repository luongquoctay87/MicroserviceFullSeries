package vn.tayjava.controller.request;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserUpdateDTO extends UserCreationRequestDTO implements Serializable {
    private Long id;
}
