package org.booking.search_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    @Field(type = FieldType.Long, name = "id")
    private Long id;

    @Field(type = FieldType.Integer, name = "bedroomCount")
    private int bedroomCount;

    @Field(type = FieldType.Integer, name = "bedCount")
    private int bedCount;

    @Field(type = FieldType.Integer, name = "maxGuestsCount")
    private int maxGuestsCount;

    @Field(type = FieldType.Double, name = "price")
    private BigDecimal price;
}
