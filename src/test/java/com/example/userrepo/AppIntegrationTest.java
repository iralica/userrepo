package com.example.userrepo;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        String body =
                restTemplate.postForEntity(
                        "http://localhost:" + port + "/users",
                        request,
                        String.class
                ).getBody();

        assertEquals(body, "User is valid");
    }
    // http://localhost:8080/upper?text=hello
    @Test
    public void testToUpperCase() throws Exception {
        ResponseEntity<String> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/upper?text=hello",
                        String.class
                );
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.hasBody());

        JSONAssert.assertEquals(response.getBody(), "{\"result\":\"HELLO\"}", true);

        JSONObject body = new JSONObject(response.getBody());

        assertTrue(body.has("result"));
        assertEquals(body.get("result"), "HELLO");

    }

    @Test
    public void testToUpperCaseWithEmpty() throws Exception {
        ResponseEntity<String> response =
                restTemplate.getForEntity(
                        "http://localhost:" + port + "/upper?text=",
                        String.class
                );

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(response.hasBody());

        JSONAssert.assertEquals(response.getBody(), "{\"result\":\"\"}", true);
    }

}
