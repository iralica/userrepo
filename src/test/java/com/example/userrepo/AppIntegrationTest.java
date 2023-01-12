package com.example.userrepo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)// для запуска тестов под JUNIT4
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// запускать тесты на не занятом случайном порту
public class AppIntegrationTest {

    @Value(value = "${local.server.port}") //получаем номер порта на котором запускается спринг
    private int port;

    //TestRestTemplate класс для выполнения
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createUserTest() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json");

        // тело запроса + заголовки + url
        HttpEntity<String> request = new HttpEntity<>(
                "{\"name\":\"rob\", \"email\":\"rob@email.com\"}",
                headers
        );



        restTemplate.postForEntity(
                "http://localhost:"+port+"/users",
                      request,
                      String.class

        ).getBody();


        //assertEquals(body, "User is valid");

    }


}
