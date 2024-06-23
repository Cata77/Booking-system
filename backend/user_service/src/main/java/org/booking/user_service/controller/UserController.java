package org.booking.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.booking.user_service.model.User;
import org.booking.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody User user) {
        service.createUser(user);

        return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
    }
}
