package com.example.userrepo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)// для запуска тестов под JUNIT4
@DataJpaTest
public class DatabaseTest {
    @Autowired
    UserRepository repository;

    @Test
    public void testUserCreation() throws Exception {
        User user = new User();
        user.setName("sveta");
        user.setEmail("sveta@google.com");

        User savedUser = repository.save(user);

        assertEquals(savedUser.getId(), 1L);
    }


}
