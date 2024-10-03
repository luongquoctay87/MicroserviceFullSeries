package vn.tayjava.service;

import jakarta.servlet.http.HttpServletRequest;
import vn.tayjava.controller.request.LoginRequest;
import vn.tayjava.controller.response.TokenResponse;

public interface AuthenticationService {

    TokenResponse createAccessToken(LoginRequest request);

    TokenResponse createRefreshToken(HttpServletRequest request);
}
