SELECT 'room' AS type, * FROM room
JOIN hotel h ON hotel_id = h.id
WHERE h.last_update > :sql_last_value