package versatile_development.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("${swagger.title}")
    private String title;

    @Value("${swagger.app.version}")
    private String version;

    @Value("${spring.mail.username}")
    private String email;

    @Value("${swagger.username}")
    private String username;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title(title)
                                .version(version)
                                .contact(
                                        new Contact()
                                                .email(email)
                                                .name(username))
                );
    }

}