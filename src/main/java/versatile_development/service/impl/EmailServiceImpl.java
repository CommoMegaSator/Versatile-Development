package versatile_development.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import versatile_development.service.EmailService;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;

import static versatile_development.constants.Constants.EMAIL_MESSAGE_CONTENT;
import static versatile_development.constants.Constants.REGISTRATION_EMAIL_SUBJECT;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final Message message;

    @Value("${spring.mail.username}")
    private String usernameThatSends;

    @Autowired
    public EmailServiceImpl(Message message) {
        this.message = message;
    }

    @Override
    public void sendEmail(String emailTo, String subject, String messageText) {
        try{
            message.setFrom(new InternetAddress(usernameThatSends));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
            message.setSubject(REGISTRATION_EMAIL_SUBJECT);
            message.setContent(messageText, EMAIL_MESSAGE_CONTENT);

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
