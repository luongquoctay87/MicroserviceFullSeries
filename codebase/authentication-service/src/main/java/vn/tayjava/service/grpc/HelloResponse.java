package vn.tayjava.service.grpc;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class HelloResponse implements Serializable {
    private String greeting;
}
