package vn.tayjava.service.grpc;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class HelloRequest implements Serializable {
    private String firstName;
    private String lastName;
}
