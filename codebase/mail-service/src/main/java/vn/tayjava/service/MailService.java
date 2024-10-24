package vn.tayjava.service;

public interface MailService {

    String sendBySendGrid(String toEmail, String subject, String body);
}
