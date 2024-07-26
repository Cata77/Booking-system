package org.booking.search_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "hotel_features")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotelFeature {

    @Id
    @Field(type = FieldType.Long, name = "id")
    private Long id;

    @Field(type = FieldType.Long, name = "hotel_id")
    private Long hotelId;

    @Field(type = FieldType.Long, name = "feature_id")
    private Long featureId;
}
