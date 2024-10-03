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
    public void testGRPC() {
//        return grpcClientTest.testingGRPC();
        grpcClientTest.verifyToken("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjpbeyJhdXRob3JpdHkiOiJtYW5hZ2VyIn1dLCJ1c2VySWQiOjIsInN1YiI6ImFkbWluIiwiaWF0IjoxNzI3OTUzNDUzLCJleHAiOjE3Mjc5NTM1MTN9.f-Fa1Gts8hC0kyaLIoAfl1oPoiM6Qp0Ht6TIEYDVnec", "ACCESS_TOKEN");
    }
}
