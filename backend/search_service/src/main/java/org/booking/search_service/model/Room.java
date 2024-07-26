package org.booking.search_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Document(indexName = "rooms")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @Field(type = FieldType.Long, name = "id")
    private Long id;

    @Field(type = FieldType.Long, name = "hotel_id")
    private Long hotelId;

    @Field(type = FieldType.Integer, name = "bedroom_count")
    private int bedroomCount;

    @Field(type = FieldType.Integer, name = "bed_count")
    private int bedCount;

    @Field(type = FieldType.Integer, name = "max_guests_count")
    private int maxGuestsCount;

    @Field(type = FieldType.Double, name = "price")
    private BigDecimal price;
}
