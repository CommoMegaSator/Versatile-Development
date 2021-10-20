package versatile_development.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import versatile_development.service.EmailService;

import javax.mail.MessagingException;

import static versatile_development.constants.Constants.EMAIL_MESSAGE_CONTENT;
import static versatile_development.constants.Constants.REGISTRATION_EMAIL_SUBJECT;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String usernameThatSends;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String emailTo, String subject, String message) {
        try{
            var mimeMessage = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mimeMessage, "utf-8");

            mimeMessage.setContent(message, EMAIL_MESSAGE_CONTENT);
            helper.setTo(emailTo);
            helper.setSubject(REGISTRATION_EMAIL_SUBJECT);
            helper.setFrom(usernameThatSends);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendEmail(SimpleMailMessage email) {
        mailSender.send(email);
    }
}
