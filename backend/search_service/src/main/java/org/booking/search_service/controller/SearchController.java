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
        @RequestParam(required = false) String name
    ) {
        SearchHits<Hotel> searchHits = service.search(name);
        return new ResponseEntity<>(searchHits, HttpStatus.OK);
    }
}
