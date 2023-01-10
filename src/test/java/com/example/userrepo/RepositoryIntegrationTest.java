package com.example.userrepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc // для создания MockMvc
public class RepositoryIntegrationTest {
    // будем вызывать методы контроллера для добавления пользователей
    // проверим что пользователи добавились в репо

    @Autowired
    UserRepository repository;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void addTwoUsers() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/users")
                                .content("{ \"name\":\"bob\", \"email\":\"bob@google.com\"  }")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk());
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/users")
                                .content("{ \"name\":\"rob\", \"email\":\"rob@google.com\"  }")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk()) // проверка
        ;


        assertThat(repository.count(), is(2L));

        // имена их rob и bob
        assertThat(repository.findAll(), containsInAnyOrder(
                        hasProperty("name", is("bob")),
                        hasProperty("name", is("rob"))
                )
        );

    }
}




