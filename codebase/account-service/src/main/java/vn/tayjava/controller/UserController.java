package vn.tayjava.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tayjava.service.grpc.GrpcClientTest;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final GrpcClientTest grpcClientTest;

    @GetMapping("/list")
    public String getAll() {
        return "user list";
    }

    @PostMapping("/register")
    public long register() {
        return 0;
    }

    @GetMapping("/test-grpc")
    public String testGRPC() {
        return grpcClientTest.testingGRPC();
    }
}
