package vn.tayjava.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vn.tayjava.service.MailService;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @GetMapping("/send")
    public String sendEmail(@RequestParam String toEmail, @RequestParam String subject, @RequestParam String body) {
       return mailService.sendBySendGrid(toEmail, subject, body);
    }

    @GetMapping("/simple-send")
    public String sendEmail() {
        return "sent";
    }
}
