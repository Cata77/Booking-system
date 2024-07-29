SELECT
    hotel.id AS hotel_id,
    hotel.name AS hotel_name,
    hotel.country,
    hotel.city,
    hotel.address,
    hotel.hotel_category,
    hotel.accommodation_type,
    hotel.property_type,
    hotel.description,
    r.id AS room_id,
    r.bedroom_count,
    r.bed_count,
    r.max_guests_count,
    r.price,
    feature.id AS feature_id,
    feature.name AS feature_name
FROM
    hotel
        JOIN
    room r ON r.hotel_id = hotel.id
        LEFT JOIN
    hotel_feature hf ON hotel.id = hf.hotel_id
        LEFT JOIN
    feature ON hf.feature_id = feature.id
WHERE
    hotel.last_update > :sql_last_value
