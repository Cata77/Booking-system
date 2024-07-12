package org.booking.hotel_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "feature")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Feature implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "feature", cascade = CascadeType.ALL)
    private Set<HotelFeature> hotelFeatures = new HashSet<>();
}
