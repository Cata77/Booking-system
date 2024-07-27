SELECT hotel.*, r.*, feature.name AS feature_name
FROM hotel
JOIN room r ON r.hotel_id = hotel.id
LEFT JOIN hotel_feature hf ON hotel.id = hf.hotel_id
LEFT JOIN feature ON hf.feature_id = feature.id
WHERE hotel.last_update > :sql_last_value