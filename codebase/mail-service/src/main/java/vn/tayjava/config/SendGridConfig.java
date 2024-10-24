package vn.tayjava.config;

import com.sendgrid.SendGrid;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SendGridConfig {

    @Bean
    SendGrid sendGrid() {
        System.out.println("API Key: " + System.getenv("SENDGRID_API_KEY"));
        return new SendGrid(System.getenv("SENDGRID_API_KEY"));
    }
}
