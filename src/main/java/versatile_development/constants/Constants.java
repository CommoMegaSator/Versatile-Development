package versatile_development.constants;

public class Constants {
    public static final int DAY = (1000 * 60 * 60 * 24);
    public static final String EMAIL_MESSAGE= "Hello <strong>%s</strong>! Welcome to <strong style=\"color: #fd7e14; font-size:16px;\">Versatile family</strong>;)\n" +
                                                "<br>To confirm your e-mail address, please click " +
                                                "<a href='%sconfirm?token=%s'>here</a>.";
    public static final String USER_LOCALE_EXTENSION = "_locale_tag";
    public static final String EMAIL_MESSAGE_CONTENT = "text/html";
    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SMTP_SOCKET_PORT = "mail.smtp.socketFactory.port";
    public static final String MAIL_SMTP_SOCKET_CLASS = "mail.smtp.socketFactory.class";
    public static final String MAIL_DEBUG = "mail.debug";
    public static final String REGISTRATION_EMAIL_SUBJECT = "Registration Confirmation";
    public static final String DELETING_USER_TOKEN = "nonVersatileProtecting";
    public static final String CREATOR_NICKNAME = "CommoMegaSator";
    public static final Integer MIN_YEARS_OLD_MINUS = 6;
    public static final String START_DATE_LIMIT = "1950-01-01";
}
