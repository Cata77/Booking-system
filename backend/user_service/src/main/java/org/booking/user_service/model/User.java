package org.booking.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private UUID uuid;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String country;
    private String city;
    private String address;
}
