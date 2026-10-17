-- Initial data helper
-- Advance the shared JHipster sequence past the highest seeded id.
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_class WHERE relkind = 'S' AND relname = 'sequence_generator') THEN
    PERFORM setval(
      'sequence_generator',
      (
        SELECT MAX(max_id) FROM (
          SELECT COALESCE(MAX(id), 1) AS max_id FROM currency
          UNION ALL SELECT COALESCE(MAX(id), 1) FROM country
          UNION ALL SELECT COALESCE(MAX(id), 1) FROM tadig_code
          UNION ALL SELECT COALESCE(MAX(id), 1) FROM network_identity
          UNION ALL SELECT COALESCE(MAX(id), 1) FROM lifecycle
          UNION ALL SELECT COALESCE(MAX(id), 1) FROM states
          UNION ALL SELECT COALESCE(MAX(id), 1) FROM state_transitions
          UNION ALL SELECT COALESCE(MAX(id), 1) FROM unit_type
          UNION ALL SELECT COALESCE(MAX(id), 1) FROM billing_cycle
        ) seeded
      )
    );
  END IF;
END $$;
