package com.example.userrepo;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
//@SpringBootTest - стартует спринг приложение целиком
//@SpringBootTest
//@AutoConfigureMockMvc - стартует спринг приложение но не стартует веб-сервер
//@WebMvcTest - стартует только веб-сервер и нужна для тестирования бизнес-логики в контроллерах
//@DataJpaTest - стартует только часть проекта по работе с базой данных

@RestController
    public class UserController {

        @Autowired
        private UserRepository repository;

        @GetMapping("/users")
        public ResponseEntity<Iterable<User>> getAll()
        {
            return ResponseEntity.ok(repository.findAll());
        }


        @PostMapping("/users")
        ResponseEntity<String> addUser(
               @Valid @RequestBody User user
        )
        {
            repository.save(user);
            return ResponseEntity.ok(
                    "User is valid"
            );
        }

    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(
                error ->
                        errors.put(
                                ((FieldError) error).getField(),
                                error.getDefaultMessage()
                        )
        );
        return errors;
    }

    // htpp://localhost:users/1 -> "bob:bob@gmail.com"
    @GetMapping("/users/{key}")
    ResponseEntity<String> nameEmailCombo(
            @PathVariable (name = "key") Long key
    )
    {
        return ResponseEntity.ok(
                repository.findById(key).map(u -> u.getName()+":"+u.getEmail()).orElse("")
        );
    }

    // 1. напишите функцию обрабатывающую запрос
    // GET http://localhost:8080/upper?text=hello -> {"result": "HELLO"}
    // 2. напишите тест который это проверит
    @GetMapping("/upper")
    public ResponseEntity<Map<String, String>> toUpperCase(
            @RequestParam /*(name = "text")*/ String text
    )
    {
        Map<String, String> result = new HashMap<>();
        result.put("result", text != null ? text.toUpperCase() : "");
        return ResponseEntity.ok(result);
    }




}


