package vn.tayjava;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RefreshScope
@RestController
public class WelcomeController {

    private final RestTemplate restTemplate;

    public WelcomeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/welcome")
    public String welcome() {
        System.out.println("================> Authentication service");
        return "Welcome to authentication service";
    }

    @CircuitBreaker(name = "accountServiceCircuitBreaker", fallbackMethod = "errorMessage")
    @GetMapping("/call-account-service")
    public String callAccountService() {
        String response = restTemplate.getForObject("http://localhost:4953/account/service-name", String.class);
        return "<h1>Hello from the " + response + "!</h1>";
    }

    @GetMapping("/db-info")
    public String getDBInfo(@Value("${spring.datasource.url}") String url,
                            @Value("${spring.datasource.username}") String username,
                            @Value("${spring.datasource.password}") String password) {
        return String.format("url=%s, username=%s, password=%s", url, username, password);
    }

    /**
     * Fall back method
     *
     * @param throwable
     * @return string
     */
    public String errorMessage(Throwable throwable) {
        return "Service unavailable, please try again!";
    }

}
