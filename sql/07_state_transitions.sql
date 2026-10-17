-- Initial data for state_transitions
-- Generated from dump-elektradb-202610121308.sql
-- Run after the JHipster tables exist. Load in filename order.


INSERT INTO state_transitions (id, name, from_state, to_state, is_allowed, lifecycle_id, created_date, modified_date)
VALUES
(1301, 'Draft-Under Review', 'Draft', 'Under Review', TRUE, 1201, '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1302, 'Draft-Deleted', 'Draft', 'Deleted', TRUE, 1201, '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1303, 'Under Review-Draft', 'Under Review', 'Draft', TRUE, 1201, '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1304, 'Under Review-Approved', 'Under Review', 'Approved', TRUE, 1201, '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1305, 'Approved-Locked', 'Approved', 'Locked', TRUE, 1201, '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1306, 'Approved-Ceased', 'Approved', 'Ceased', TRUE, 1201, '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1307, 'Approved-Draft', 'Approved', 'Draft', TRUE, 1201, '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1308, 'Locked-Ceased', 'Locked', 'Ceased', TRUE, 1201, '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1309, 'Locked-Draft', 'Locked', 'Draft', TRUE, 1201, '2021-08-24 05:13:04', '2021-08-24 05:13:04'),
(1310, 'Ceased-Deleted', 'Ceased', 'Deleted', TRUE, 1201, '2021-08-24 05:13:04', '2021-08-24 05:13:04')
ON CONFLICT (id) DO NOTHING;
