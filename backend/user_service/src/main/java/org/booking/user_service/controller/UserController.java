package org.booking.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.booking.user_service.model.User;
import org.booking.user_service.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/profile")
    @PreAuthorize("hasRole('user')")
    public ResponseEntity<User> showUserProfile() {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        User userProfile = service.extractUserDetailsFromJwt(jwt);
        return new ResponseEntity<>(userProfile, HttpStatus.OK);
    }
}
