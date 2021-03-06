package versatile_development.controller;

import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import redis.clients.jedis.Jedis;
import versatile_development.entity.nasa.PictureOfDay;
import versatile_development.service.NasaService;

@Slf4j
@NoArgsConstructor
@Controller
public class NasaController {

    @Autowired
    NasaService nasaService;

    Jedis jedis = new Jedis();
    Gson gson = new Gson();;

    @GetMapping("/picture-of-day")
    public String getPictureOfDayFromNasa(Model model){
        String json = jedis.get("pictureOfDay");

        if (json == null){
            nasaService.requestForPictureOfDay();
            json = jedis.get("pictureOfDay");
        }

        PictureOfDay pictureOfDay = gson.fromJson(json, PictureOfDay.class);

        model.addAttribute(pictureOfDay);
        return "picture_of_day";
    }
}
