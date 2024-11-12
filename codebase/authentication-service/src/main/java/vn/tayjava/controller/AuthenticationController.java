package vn.tayjava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.tayjava.common.TokenType;
import vn.tayjava.controller.request.LoginRequest;
import vn.tayjava.controller.response.TokenResponse;
import vn.tayjava.service.AuthenticationService;

import static org.springframework.http.HttpStatus.OK;

@RestController
@Slf4j
@Tag(name = "Authentication Controller")
public record AuthenticationController(AuthenticationService authenticationService) {

    @Operation(summary = "Access Token", description = "Generate access token")
    @PostMapping("/access-token")
    @ResponseStatus(OK)
    public ResponseEntity<TokenResponse> accessToken(@RequestBody LoginRequest request) {
        log.info("POST /access-token");
        return new ResponseEntity<>(authenticationService.createAccessToken(request), OK);
    }

    @Operation(summary = "Refresh Token", description = "Generate refresh token")
    @PostMapping("/refresh-token")
    @ResponseStatus(OK)
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        log.info("POST /refresh-token");
        return new ResponseEntity<>(authenticationService.createRefreshToken(request), OK);
    }

    @GetMapping("/test-cors")
    public ResponseEntity<TokenResponse> cors() {

        return new ResponseEntity<>(TokenResponse.builder().accessToken("ACCESSTOKEN").refreshToken("REFRESHTOKEN").build(), OK);
    }

    @GetMapping("/test-delay")
    public ResponseEntity<String> delay() throws InterruptedException {
        Thread.sleep(10000);
        return new ResponseEntity<>("Delayed !!!", OK);
    }
}