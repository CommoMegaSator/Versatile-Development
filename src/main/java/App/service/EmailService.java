package App.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    void sendEmail(SimpleMailMessage email);
    void sendEmail(String emailTo, String subject, String message);
}
