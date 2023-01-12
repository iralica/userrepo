package com.example.userrepo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)// для запуска тестов под JUNIT4
@WebMvcTest // загружается только веб часть
@AutoConfigureMockMvc

public class TestUserController {
    @Autowired
    MockMvc mockMvc; // возможность выполнять запросы в контроллере не загружая веб сервер

    @Autowired
    UserController userController;

    @Test
    public void normalUserCreation() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .content("{ \"name\":\"bob\", \"email\":\"bob@google.com\"  }")
                                .contentType("application/json")

                )
                .andExpect(status().isOk());
    }

    @Test
    public void noNameIsError() throws Exception {

    }

    public void noEmailIsError() throws Exception {

    }

    public void notValidEmailIsError() throws Exception {

    }

}
