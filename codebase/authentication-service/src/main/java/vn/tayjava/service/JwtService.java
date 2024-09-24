package vn.tayjava.service;


import org.springframework.security.core.GrantedAuthority;
import vn.tayjava.common.TokenType;

import java.util.Collection;

public interface JwtService {

    String generateToken(Long userId, String username, Collection<? extends GrantedAuthority> authorities);

    String generateRefreshToken(Long userId, String username, Collection<? extends GrantedAuthority> authorities);

    String extractUsername(String token, TokenType type);

    boolean isValid(String token, TokenType type, String username);
}
