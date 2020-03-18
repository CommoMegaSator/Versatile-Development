package versatile_development.config;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import redis.clients.jedis.Jedis;
import versatile_development.constants.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class ApplicationLocaleResolver extends SessionLocaleResolver {
    private Jedis jedis;

    ApplicationLocaleResolver(){
        super();
        jedis = new Jedis();
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String userName = securityContext.getAuthentication().getName();
        String userLocaleKey = userName + Constants.USER_LOCALE_EXTENSION;
        String localeOption = "en_US";

        if (jedis.exists(userLocaleKey))localeOption = jedis.get(userLocaleKey);
        else jedis.set(userLocaleKey, localeOption);

        Locale userLocale = org.springframework.util.StringUtils.parseLocaleString(localeOption);
        return userLocale;
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        super.setLocale(request, response, locale);

        SecurityContext securityContext = SecurityContextHolder.getContext();
        String userName = securityContext.getAuthentication().getName();
        String userLocaleKey = userName + Constants.USER_LOCALE_EXTENSION;

        jedis.set(userLocaleKey, String.valueOf(locale));
    }

}
