package versatile_development.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import versatile_development.service.UserService;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityJavaConfig extends WebSecurityConfigurerAdapter {

    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @Autowired
    SecurityJavaConfig(PasswordEncoder passwordEncoder, UserService userService){
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/registration", "/confirm", "/static/**", "/reset",
                        "/swagger-resources/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/profile", true)
                    .passwordParameter("password")
                    .usernameParameter("username")
                .and()
                .rememberMe()
                    .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                    .rememberMeParameter("remember-me")
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
                    .clearAuthentication(true)
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID", "remember-me")
                    .logoutSuccessUrl("/login");
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }
}
