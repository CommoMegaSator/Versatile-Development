package versatile_development.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import static versatile_development.constants.Constants.*;

@Configuration
public class MailConfig {

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.debug}")
    private String debug;

    @Value("${mail.smtp.socketFactory.port}")
    private String socketFactoryPort;

    @Value("${mail.smtp.socketFactory.class}")
    private String socketFactoryClass;

    @Value("${mail.smtp.auth}")
    private String auth;

    @Bean
    public Message getMessage(){
        Properties prop = new Properties();
        prop.put(MAIL_SMTP_HOST, host);
        prop.put(MAIL_SMTP_PORT, port);
        prop.put(MAIL_SMTP_AUTH, auth);
        prop.put(MAIL_SMTP_SOCKET_PORT, socketFactoryPort);
        prop.put(MAIL_SMTP_SOCKET_CLASS, socketFactoryClass);
        prop.put(MAIL_DEBUG, debug);

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        return new MimeMessage(session);
    }
}
