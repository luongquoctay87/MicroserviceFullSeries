package vn.tayjava.config;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.*;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import vn.tayjava.service.VerifyTokenService;

import java.nio.charset.StandardCharsets;
import java.util.*;


@Component
@Slf4j
public class ApiRequestFilter extends AbstractGatewayFilterFactory<ApiRequestFilter.Config> {

    private final RestTemplate restTemplate;
    private final VerifyTokenService verifyTokenService;

    public ApiRequestFilter(RestTemplate restTemplate, VerifyTokenService verifyTokenService) {
        super(Config.class);
        this.restTemplate = restTemplate;
        this.verifyTokenService = verifyTokenService;
    }

    private List<String> permitUrls = List.of("/user/register");

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String url = request.getPath().toString();
            log.info("-------------[ {} ]", url);

            if (permitUrls.contains(url)) {
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

//                // TODO sau nay se goi bang gRPC
//                String verifyUrl = String.format("http://localhost:8081/verify-token?token=%s", token);
//
//                Boolean result = restTemplate.getForObject(verifyUrl, Boolean.class);
//
//                assert result != null;
//                if (!result) {
//                    return error(exchange.getResponse(), url, "Token invalid");
//                }

                boolean accessToken = verifyTokenService.verifyToken(token, "ACCESS_TOKEN");
                if (!accessToken) {
                    return error(exchange.getResponse(), url, "Token invalid");
                }

                // Authorization
                // 1. TODO decode token lay id role cua user
                // 2. check user co quyen access cai url nay khong (permission)

                log.info("Request valid");
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                }));
            } else {
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

    public static class Config {

    }
}
