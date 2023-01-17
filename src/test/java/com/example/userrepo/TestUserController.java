package com.example.userrepo;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
@RunWith(SpringRunner.class)// для запуска тестов под JUNIT4
@WebMvcTest // загружается только веб часть
@AutoConfigureMockMvc

public class TestUserController {
    @Autowired
    MockMvc mockMvc; // возможность выполнять запросы в контроллере не загружая веб сервер

    @Autowired
    UserController userController;

    @MockBean  // нужно заменить реализацию UserRepository на заглушку
            // создает фэйковый репоситори чтобы тест работал
            // это нужно чтобы можно было тестировать метода контроллера не создавая в памяти часть
            // отвечающую за базу данных
    UserRepository repository;

    @Test
    public void normalUserCreation() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .content("{ \"name\":\"bob\", \"email\":\"bob@google.com\"  }")
                                .contentType("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("User is valid"))
                 ;
    }

    @Test
    public void noNameIsError() throws Exception {
        mockMvc.perform(
                post("/users")
                        .content("{ \"email\":\"bob@google.com\"  }")
                        .contentType("application/json")

        )
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.name", Is.is("Name is mandatory"))
                )
                        //.andDo(print())
                ;
    }
    @Test
    public void noEmailIsError() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .content("{ \"name\":\"bob\"  }")
                                .contentType("application/json")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.email", Is.is("Email is mandatory"))
                )
        ;
    }

    @Test
    public void notValidEmailIsError() throws Exception {
       // "{ \"name\":\"bob\", \"email\":\"123\"  }"
        mockMvc.perform(
                        post("/users")
                                .content("{\"name\":\"bob\", \"email\":\"123\"}")
                                .contentType("application/json")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.email", Is.is("Email should be valid"))
                )
        ;
    }

    public void blankUserIsError() throws Exception {
        mockMvc.perform(
                        post("/users")
                                .content("{}")
                                .contentType("application/json")
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.email", Is.is("Email is mandatory"))
                )
                .andExpect(
                        jsonPath("$.name", Is.is("Name is mandatory"))
                )
        ;
    }

   @Test
    public void testNameEmailCombo() throws Exception {
        // userRepository.findById(1L) -> User("rob", "rob@gmail.com")

        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(new User(1L, "rob", "rob@gmail.com"))
                );
//        when(userRepository.findById(anyLong()))
//                .thenReturn(
//                        Optional.of(new User(1L, "rob", "rob@gmail.com")),
//                        Optional.of(new User(2L, "bob", "bob@gmail.com"))
//                );
        doReturn(
                Optional.of(new User(1L, "rob", "rob@gmail.com"))
        ).when(repository).findById(10L);

        // "/users/1" -> "rob:rob@gmail.com"
        mockMvc.perform(
                        get("/users/1")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("rob:rob@gmail.com"));

        // метод repository.findById(1L) вызывался только один раз
        verify(repository, times(1)).findById(1L);
        // больше никаких взаимодействий с userRepository не было,
        // и никакие его методы более не вызывались
        verifyNoMoreInteractions(repository);
    }





}
