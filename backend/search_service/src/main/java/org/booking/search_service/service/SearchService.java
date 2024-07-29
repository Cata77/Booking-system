package org.booking.search_service.service;

import lombok.RequiredArgsConstructor;
import org.booking.search_service.model.Hotel;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchOperations elasticsearchOperations;

    public SearchHits<Hotel> search(String name, String country, String city, String address,
            String hotelCategory, String accommodationType, String propertyType, String description,
            String checkInTime, String checkOutTime, String bedroomCount, String bedCount,
            String maxGuestsCount, String price, String feature
    ) {
        Criteria criteria = buildCriteria(name, country, city, address, hotelCategory, accommodationType,
                propertyType, description, checkInTime, checkOutTime, bedroomCount, bedCount,
                maxGuestsCount, price, feature);
        Query query = new CriteriaQuery(criteria);
        return elasticsearchOperations.search(query, Hotel.class);
    }

    private Criteria buildCriteria(String name, String country, String city, String address,
            String hotelCategory, String accommodationType, String propertyType, String description,
            String checkInTime, String checkOutTime, String bedroomCount, String bedCount,
            String maxGuestsCount, String price, String feature
    ) {
        Criteria criteria = new Criteria();

        if (name != null && !name.isEmpty()) {
            criteria = criteria.and("name").contains(name);
        }

        if (country != null && !country.isEmpty()) {
            criteria = criteria.and("country").is(country);
        }

        if (city != null && !city.isEmpty()) {
            criteria = criteria.and("city").is(city);
        }

        if (address != null && !address.isEmpty()) {
            criteria = criteria.and("address").is(address);
        }

        if (hotelCategory != null && !hotelCategory.isEmpty()) {
            criteria = criteria.and("hotelCategory").is(hotelCategory);
        }

        if (accommodationType != null && !accommodationType.isEmpty()) {
            criteria = criteria.and("accommodationType").is(accommodationType);
        }

        if (propertyType != null && !propertyType.isEmpty()) {
            criteria = criteria.and("propertyType").is(propertyType);
        }

        if (description != null && !description.isEmpty()) {
            criteria = criteria.and("description").is(description);
        }

        if (checkInTime != null && !checkInTime.isEmpty()) {
            criteria = criteria.and("checkInTime").is(checkInTime);
        }

        if (checkOutTime != null && !checkOutTime.isEmpty()) {
            criteria = criteria.and("checkOutTime").is(checkOutTime);
        }

        if (bedroomCount != null && !bedroomCount.isEmpty()) {
            criteria = criteria.and("bedroom_count").lessThanEqual(bedroomCount);
        }

        if (bedCount != null && !bedCount.isEmpty()) {
            criteria = criteria.and("bed_count").lessThanEqual(bedCount);
        }

        if (maxGuestsCount != null && !maxGuestsCount.isEmpty()) {
            criteria = criteria.and("max_guests_count").lessThanEqual(maxGuestsCount);
        }

        if (price != null && !price.isEmpty()) {
            criteria = criteria.and("price").lessThanEqual(price);
        }

        if (feature != null && !feature.isEmpty()) {
            criteria = criteria.and("feature").is(feature);
        }

        return criteria;
    }
}
