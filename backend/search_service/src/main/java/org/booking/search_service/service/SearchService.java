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

    public SearchHits<Hotel> search(String name) {
        Criteria criteria = new Criteria("name").is(name);
        Query query = new CriteriaQuery(criteria);
        return elasticsearchOperations.search(query, Hotel.class);
    }
}
