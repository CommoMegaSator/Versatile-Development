package versatile_development.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import versatile_development.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String usernameThatSends;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String emailTo, String subject, String message) {
        SimpleMailMessage registrationEmail = new SimpleMailMessage();
        registrationEmail.setTo(emailTo);
        registrationEmail.setSubject("Registration Confirmation");
        registrationEmail.setText(message);
        registrationEmail.setFrom(usernameThatSends);

        mailSender.send(registrationEmail);
    }

    @Override
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }
}
