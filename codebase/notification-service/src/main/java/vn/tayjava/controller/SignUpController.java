package vn.tayjava.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.tayjava.service.SnsService;

@RestController
@RequestMapping("/sns")
public record SignUpController(SnsService snsClient) {

    @PostMapping("/test-sns")
    public void testSNS() {

        String platformArn = "arn:aws:sns:eu-west-1:545009841427:app/GCM/android_platform_dev"; // Get ARN from amazon SNS platform
        String message = "Welcome to SNS";
        String deviceToken = "ios-device-token";

        snsClient.sendToSNS(platformArn, deviceToken, message);
    }
}
