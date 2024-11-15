package vn.tayjava.config;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import vn.tayjava.grpcserver.VerifyResponse;
import vn.tayjava.service.VerifyTokenService;

import java.nio.charset.StandardCharsets;
import java.util.*;


@Component
@Slf4j
public class ApiRequestFilter extends AbstractGatewayFilterFactory<ApiRequestFilter.Config> {

    private final VerifyTokenService verifyTokenService;

    public ApiRequestFilter(VerifyTokenService verifyTokenService) {
        super(Config.class);
        this.verifyTokenService = verifyTokenService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String url = request.getPath().toString();
            log.info("-------------[ {} ]", url);

            if (isWhiteListURL(url) || url.contains("/v3/api-docs/")) {
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                }));
            }

            ServerHttpResponse response = exchange.getResponse();
            HttpMethod httpMethod = request.getMethod();
            HttpHeaders httpHeaders = response.getHeaders();
            HttpHeaders requestHeaders = request.getHeaders();

            if (requestHeaders.containsKey("Authorization")) {
                HttpHeaders headers = new HttpHeaders();
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                HttpEntity<String> entity = new HttpEntity<>(headers);

                // authentication
                final String token = request.getHeaders().getOrEmpty("Authorization").get(0).substring(7);

                VerifyResponse verifyToken = verifyTokenService.verifyToken(token);
                if (!verifyToken.getIsVerified()) {
                    return error(exchange.getResponse(), url, verifyToken.getMessage());
                }

                httpHeaders.add("userId", "10" );

                // TODO authorization

                log.info("Request valid");
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                }));
            } else {
                log.info("Request not valid, URL={}", url);
                return error(exchange.getResponse(), url, "Request invalid, Please try again!");
            }
        };
    }

    private Mono<Void> error(ServerHttpResponse response, String url, String message) {
        Map<String, Object> errors = new LinkedHashMap<>();
        errors.put("timestamp", new Date());
        errors.put("path", url);
        errors.put("status", 401);
        errors.put("error", "Unauthorized");
        errors.put("message", message);

        byte[] bytes = new Gson().toJson(errors).getBytes(StandardCharsets.UTF_8);

        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        response.setStatusCode(HttpStatus.OK);

        return response.writeWith(Mono.just(buffer));
    }

    /**
     * White list for access app without token
     * @return
     */
    private boolean isWhiteListURL(String url) {
        List<String> permitUrls = new LinkedList<>();
        permitUrls.add("/access-token");
        permitUrls.add("/refresh-token");
        permitUrls.add("/test-cors");

        return permitUrls.contains(url);
    }

    public static class Config {

    }
}
