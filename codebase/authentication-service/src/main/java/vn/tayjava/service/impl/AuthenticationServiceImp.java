package vn.tayjava.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import vn.tayjava.controller.request.LoginRequest;
import vn.tayjava.controller.response.TokenResponse;
import vn.tayjava.exception.InvalidDataException;
import vn.tayjava.model.RedisToken;
import vn.tayjava.repository.TokenRepository;
import vn.tayjava.repository.UserRepository;
import vn.tayjava.service.AuthenticationService;
import vn.tayjava.service.JwtService;

import java.util.List;

import static org.springframework.http.HttpHeaders.REFERER;
import static vn.tayjava.common.TokenType.ACCESS_TOKEN;
import static vn.tayjava.common.TokenType.REFRESH_TOKEN;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImp implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public TokenResponse createAccessToken(LoginRequest request) {

        var user = userRepository.findByUsername(request.getUsername());

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword(), user.getAuthorities()));

        // generate access token
        String accessToken = jwtService.generateToken(user.getId(), user.getUsername(), user.getAuthorities());

        // generate refresh token
        String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getUsername(), user.getAuthorities());

        List<String> roleList = user.getRoles().stream().map(role -> role.getRole().getName()).toList();

        // save token with difference versions (WEB, MOBILE, MiniApp) to DB
        tokenRepository.save(RedisToken.builder()
                .id(request.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .flatForm(request.getPlatform().getValue())
                .deviceToken(request.getDeviceToken())
                .roles(roleList.toString())
                .build());

        // TODO how to manage token for multiple devices
        // TODO how to manage authorization for APIs

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public TokenResponse createRefreshToken(HttpServletRequest request) {
        final String refreshToken = request.getHeader(REFERER);

        if (StringUtils.isBlank(refreshToken)) {
            throw new InvalidDataException("Token must be not blank");
        }

        final String userName = jwtService.extractUsername(refreshToken, REFRESH_TOKEN);
        var user = userRepository.findByUsername(userName);

        if (!jwtService.isValid(refreshToken, REFRESH_TOKEN, user.getUsername())) {
            throw new InvalidDataException("Not allow access with this token");
        }

        // generate access token
        String accessToken = jwtService.generateToken(user.getId(), user.getUsername(), user.getAuthorities());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public Boolean verifyToken(String token) {
        try {
            final String userName = jwtService.extractUsername(token, ACCESS_TOKEN);
            var user = userRepository.findByUsername(userName);

            return jwtService.isValid(token, ACCESS_TOKEN, user.getUsername());
        } catch (Exception e) {
            return false;
        }
    }
}