package vn.tayjava.service.impl;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import vn.tayjava.service.MailService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private final SendGrid sendGrid;

    @Value("${spring.mail.from}")
    private String emailFrom;

//    @Value("${endpoint.confirmUser}")
    private String apiConfirmUser = "http://localhost:8082/user/confirm";

    @KafkaListener(topics = "send-email-register-topic", groupId = "send-email-register-group")
    public void sendConfirmLinkByKafka(String message) throws MessagingException, UnsupportedEncodingException {
        log.info("Sending link to user, email={}", message);

        String[] arr = message.split(",");
        String emailTo = arr[0].substring(arr[0].indexOf('=') + 1);
        String userId = arr[1].substring(arr[1].indexOf('=') + 1);
        String verifyCode = arr[2].substring(arr[2].indexOf('=') + 1);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        Context context = new Context();

        String linkConfirm = String.format("%s/%s?verifyCode=%s", apiConfirmUser, userId, verifyCode);

        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);

        helper.setFrom(emailFrom, "Tây Java");
        helper.setTo(emailTo);
        helper.setSubject("Please confirm your account");
        String html = templateEngine.process("confirm-email.html", context);
        helper.setText(html, true);

        mailSender.send(mimeMessage);
        log.info("Link has sent to user, email={}, linkConfirm={}", emailTo, linkConfirm);
    }

    @Override
    public String sendBySendGrid(String toEmail, String subject, String body) {
        log.info("Sending email to {}", toEmail);

        Email from = new Email(emailFrom);
        Email to = new Email(toEmail, "Tây Java");
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);
            if (response.getStatusCode() == 202) {
                log.info("Email sent successfully");
            } else {
                log.info("Email sent failed, body: {}", response.getBody());
            }
        } catch (IOException e) {
            log.error("Send email was failure, errorMessage={}", e.getMessage());
        }

        return "sent";
    }

}
