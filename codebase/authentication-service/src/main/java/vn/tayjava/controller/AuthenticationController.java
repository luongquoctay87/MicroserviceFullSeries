package vn.tayjava.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @PostMapping("/access-token")
    public String accessToken() {
        return "Access Token";
    }
}