package vn.tayjava.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class RequestFilter extends OncePerRequestFilter {

//    private final UserService userService;
//    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        log.info("---------- doFilterInternal ----------");

//        final String authorization = request.getHeader(AUTHORIZATION);
//        //log.info("Authorization: {}", authorization);
//
//        if (StringUtils.isBlank(authorization) || !authorization.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        final String token = authorization.substring("Bearer ".length());
//        //log.info("Token: {}", token);
//
//        final String userName = jwtService.extractUsername(token, ACCESS_TOKEN);
//
//        if (StringUtils.isNotEmpty(userName) && SecurityContextHolder.getContext().getAuthentication() == null) {
//            UserDetails userDetails = userService.userDetailsService().loadUserByUsername(userName);
//            if (jwtService.isValid(token, ACCESS_TOKEN, userDetails)) {
//                SecurityContext context = SecurityContextHolder.createEmptyContext();
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                context.setAuthentication(authentication);
//                SecurityContextHolder.setContext(context);
//            }
//        }

        filterChain.doFilter(request, response);
    }
}
