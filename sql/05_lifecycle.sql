-- Initial data for lifecycle
-- Generated from dump-elektradb-202610121308.sql
-- Run after the JHipster tables exist. Load in filename order.
-- created_on / modified_on from the source are stored as created_date / modified_date.

INSERT INTO lifecycle (id, name, created_date, modified_date)
VALUES
(1201, 'Lifecycle 1', '2021-08-24 05:13:04', '2021-08-24 05:18:14')
ON CONFLICT (id) DO NOTHING;
