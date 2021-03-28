package versatile_development.service.impl;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;
import versatile_development.entity.nasa.PictureOfDay;
import versatile_development.service.NasaService;

import static versatile_development.constants.Constants.API_KEY_PARAM;
import static versatile_development.constants.Constants.PICTURE_OF_DAY;

@Slf4j
@Service
public class NasaServiceImpl implements NasaService {

    Jedis jedis = new Jedis();
    Gson gson = new Gson();
    RestTemplate restTemplate = new RestTemplate();

    @Value("${integration.nasa.api-key}")
    private String apiKey;

    @Scheduled(cron = "0 0 4 * * *")
    public void requestForPictureOfDay(){
        String nasaPictureOfDayUrl = PICTURE_OF_DAY + API_KEY_PARAM + apiKey;
        ResponseEntity<PictureOfDay> pictureOfDayResponse = restTemplate.getForEntity(nasaPictureOfDayUrl, PictureOfDay.class);

        if (pictureOfDayResponse.getStatusCodeValue() == 200) {
            jedis.set("pictureOfDay", gson.toJson(pictureOfDayResponse.getBody()));
            log.info("Picture of day updated successfully");
        }
        else {
            log.error("Something went wrong with Nasa");
        }
    }
}
