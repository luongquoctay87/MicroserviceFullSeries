package vn.tayjava;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class WelcomeController {

    @GetMapping("/welcome")
    public String welcome() {
        System.out.println("================> Account service");
        return "Welcome to account service";
    }

    @GetMapping("/service-name")
    public String getServiceName() {
        return "ACCOUNT-SERVICE";
    }

    @GetMapping("/redis-info")
    public String getRedisInfo(@Value("${redis.host}") String host,
                              @Value("${redis.port}") String port) {
        return String.format("host=%s, port=%s", host, port);
    }
}
