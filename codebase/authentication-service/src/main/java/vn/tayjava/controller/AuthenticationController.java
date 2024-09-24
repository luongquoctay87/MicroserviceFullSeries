package vn.tayjava.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
    public ResponseEntity<TokenResponse> accessToken(@RequestBody LoginRequest request) {
        log.info("POST /access-token");
        return new ResponseEntity<>(authenticationService.createAccessToken(request), OK);
    }

    @Operation(summary = "Refresh Token", description = "Generate refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<TokenResponse> refreshToken(HttpServletRequest request) {
        log.info("POST /refresh-token");
        return new ResponseEntity<>(authenticationService.createRefreshToken(request), OK);
    }
}