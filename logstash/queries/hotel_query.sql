SELECT 'hotel' AS type, * FROM hotel
WHERE last_update > :sql_last_value