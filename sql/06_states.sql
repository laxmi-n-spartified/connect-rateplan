-- Initial data for states
-- Generated from dump-elektradb-202610121308.sql
-- Run after the JHipster tables exist. Load in filename order.


INSERT INTO states (id, lc_state_name, created_date, modified_date)
VALUES
(1251, 'Draft', '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1252, 'Under Review', '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1253, 'Approved', '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1254, 'Locked', '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1255, 'Ceased', '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1256, 'Deleted', '2021-08-24 05:13:04', '2021-08-24 05:13:04')
ON CONFLICT (id) DO NOTHING;
