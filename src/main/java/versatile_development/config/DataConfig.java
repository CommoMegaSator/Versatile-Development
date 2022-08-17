package versatile_development.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

@Slf4j
@Configuration
public class DataConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public Jedis jedis(){
        Jedis jedis = new Jedis(host, Integer.valueOf(port));
        if(!password.isEmpty()){
            jedis.auth(password);
        }
        return jedis;
    }
}
