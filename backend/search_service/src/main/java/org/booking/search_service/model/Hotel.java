package org.booking.search_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Set;

@Document(indexName = "hotels")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    @Id
    @Field(type = FieldType.Long, name = "id")
    private Long id;

    @Field(type = FieldType.Text, name = "name")
    private String name;

    @Field(type = FieldType.Text, name = "country")
    private String country;

    @Field(type = FieldType.Text, name = "city")
    private String city;

    @Field(type = FieldType.Text, name = "address")
    private String address;

    @Field(type = FieldType.Text, name = "hotelCategory")
    private String hotelCategory;

    @Field(type = FieldType.Text, name = "accommodationType")
    private String accommodationType;

    @Field(type = FieldType.Text, name = "propertyType")
    private String propertyType;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    @Field(type = FieldType.Text, name = "checkInTime")
    private String checkInTime;

    @Field(type = FieldType.Text, name = "checkOutTime")
    private String checkOutTime;

    @Field(type = FieldType.Nested, name = "rooms")
    private Set<Room> rooms;

    @Field(type = FieldType.Nested, name = "features")
    private Set<Feature> features;
}
