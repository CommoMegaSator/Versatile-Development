package versatile_development.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import versatile_development.service.EmailService;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {JavaMailSender.class, EmailServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class EmailServiceImplTest {
    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender javaMailSender;

    @Test
    public void testSendEmail() throws MailException {
        doNothing().when(this.javaMailSender).send((MimeMessage) any());
        when(this.javaMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
        this.emailService.sendEmail("jane.doe@example.org", "Hello from the Dreaming Spires",
                "Not all who wander are lost");
        verify(this.javaMailSender).createMimeMessage();
        verify(this.javaMailSender).send((MimeMessage) any());
    }

    @Test
    void sendEmailTest() {
        var simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("Test");
        simpleMailMessage.setTo("Tester");
        simpleMailMessage.setText("Testing...");

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        emailService.sendEmail(simpleMailMessage);

        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}

