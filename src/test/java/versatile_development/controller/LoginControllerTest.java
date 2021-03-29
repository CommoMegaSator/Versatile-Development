package versatile_development.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import versatile_development.service.EmailService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class LoginControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @SneakyThrows
    public void registrationExpectsCreatedStatus() {
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

    @Test
    @SneakyThrows
    public void registrationExpectsConflictStatus() {
        this.mvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"firstname\":\"administrator2\",\n" +
                        "\t\"lastname\":\"administrator2\",\n" +
                        "\t\"nickname\":\"administrator2\",\n" +
                        "\t\"email\":\"admin2@ukr.net\",\n" +
                        "\t\"age\":21,\n" +
                        "\t\"password\":\"administrator2\"\n" +
                        "}}")
                .accept(MediaType.APPLICATION_JSON));

        this.mvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\t\"firstname\":\"administrator2\",\n" +
                        "\t\"lastname\":\"administrator2\",\n" +
                        "\t\"nickname\":\"administrator2\",\n" +
                        "\t\"email\":\"admin2@ukr.net\",\n" +
                        "\t\"age\":21,\n" +
                        "\t\"password\":\"administrator2\"\n" +
                        "}}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }
}