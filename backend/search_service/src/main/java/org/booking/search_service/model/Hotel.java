package org.booking.search_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalTime;
import java.util.UUID;

@Document(indexName = "hotels")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    @Id
    @Field(type = FieldType.Long, name = "id")
    private Long id;

    @Field(type = FieldType.Keyword, name = "user_id")
    private UUID userId;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "country")
    private String country;

    @Field(type = FieldType.Text, name = "city")
    private String city;

    @Field(type = FieldType.Text, name = "address")
    private String address;

    @Field(type = FieldType.Keyword, name = "hotel_category")
    private HotelCategory hotelCategory;

    @Field(type = FieldType.Keyword, name = "accommodation_type")
    private AccommodationType accommodationType;

    @Field(type = FieldType.Keyword, name = "property_type")
    private PropertyType propertyType;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    @Field(type = FieldType.Date, name = "check_in_time", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalTime checkInTime;

    @Field(type = FieldType.Date, name = "check_out_time", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalTime checkOutTime;
}
