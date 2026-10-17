-- Initial data for unit_type
-- Generated from dump-elektradb-202610121308.sql
-- Run after the JHipster tables exist. Load in filename order.
-- tenant_id from the source dump is omitted.

INSERT INTO unit_type (id, name, conversion_factor, lowest_unit, created_date, modified_date)
VALUES
(1, 'Second', 1, 'Second', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Minute', 60, 'Second', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Minutes', 60, 'Second', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Byte', 1, 'Byte', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'KB', 1024, 'Byte', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'MB', 1048576, 'Byte', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'GB', 1073741824, 'Byte', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'SMS', 1, 'SMS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, '0', 0, '0', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;
