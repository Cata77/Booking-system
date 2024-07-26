package org.booking.search_service.controller;

import lombok.RequiredArgsConstructor;
import org.booking.search_service.model.Hotel;
import org.booking.search_service.service.SearchService;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService service;

    @GetMapping
    public ResponseEntity<SearchHits<Hotel>> search(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String country,
        @RequestParam(required = false) String city,
        @RequestParam(required = false) String address,
        @RequestParam(required = false) String hotelCategory,
        @RequestParam(required = false) String accommodationType,
        @RequestParam(required = false) String propertyType,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) String checkInTime,
        @RequestParam(required = false) String checkOutTime
    ) {
        SearchHits<Hotel> searchHits = service.search(name, country, city, address, hotelCategory,
                accommodationType, propertyType, description, checkInTime, checkOutTime);
        return new ResponseEntity<>(searchHits, HttpStatus.OK);
    }
}
