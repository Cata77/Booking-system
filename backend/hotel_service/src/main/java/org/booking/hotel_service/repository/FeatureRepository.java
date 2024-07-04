package org.booking.hotel_service.repository;

import org.booking.hotel_service.model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeatureRepository extends JpaRepository<Feature, Long> {
}
