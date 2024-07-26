SELECT 'hotel_feature' AS type, * FROM hotel_feature hf
JOIN hotel h ON hotel_id = h.id
WHERE last_update > :sql_last_value