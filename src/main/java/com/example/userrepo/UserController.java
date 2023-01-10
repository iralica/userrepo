package com.example.userrepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
                @RequestBody User user
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




}


