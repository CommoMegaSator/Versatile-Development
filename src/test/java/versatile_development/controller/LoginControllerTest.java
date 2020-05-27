package versatile_development.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @Ignore
    @SneakyThrows
    public void registrationExpectsCreatedStatus(){
        this.mvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"firstname\":\"administrator\",\n" +
                        "\t\"lastname\":\"administrator\",\n" +
                        "\t\"nickname\":\"administrator\",\n" +
                        "\t\"email\":\"admin@ukr.net\",\n" +
                        "\t\"age\":21,\n" +
                        "\t\"password\":\"administrator\"\n" +
                        "}}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}