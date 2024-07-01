package org.booking.user_service.service;

import lombok.RequiredArgsConstructor;
import org.booking.user_service.model.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    public User extractUserDetailsFromJwt(Jwt jwt) {
        String subject = (String) jwt.getClaims().get("sub");
        UUID uuid = UUID.fromString(subject);
        String username = (String) jwt.getClaims().get("preferred_username");
        String firstname = (String) jwt.getClaims().get("given_name");
        String lastname = (String) jwt.getClaims().get("family_name");
        String email = (String) jwt.getClaims().get("email");
        String country = (String) jwt.getClaims().get("country");
        String city = (String) jwt.getClaims().get("city");
        String address = (String) jwt.getClaims().get("address");

        return User.builder()
                .uuid(uuid)
                .username(username)
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .country(country)
                .city(city)
                .address(address)
                .build();
    }

}
