package org.booking.hotel_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "hotel")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Hotel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;
    private UUID userId;
    private String name;
    private String country;
    private String city;
    private String address;

    @Enumerated(EnumType.STRING)
    private HotelCategory hotelCategory;

    @Enumerated(EnumType.STRING)
    private AccommodationType accommodationType;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    private String description;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Room> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<HotelFeature> hotelFeatures;
}
