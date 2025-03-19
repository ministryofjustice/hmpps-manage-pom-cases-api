insert into allocation_history (nomis_offender_id, prison, allocated_at_tier, override_reasons, override_detail, message, suitability_detail, primary_pom_name, secondary_pom_name, created_by_name, primary_pom_nomis_id, secondary_pom_nomis_id, event, event_trigger, created_at, updated_at, primary_pom_allocated_at, recommended_pom_type)

values
    ('GAX123', 'LEI', 'C', null, null, null, null, 'POM, Primary', null, null, 123, null, null, null, '2025-01-01', '2025-01-01', '2025-02-01', 'Prison'),
    ('GAX456', 'LEI', 'C', null, null, null, null, 'POM, Primary', null, null, 123, null, null, null, '2025-01-01', '2025-01-01', '2025-02-01', 'Prison'),
    ('GAX678', 'LEI', 'C', null, null, null, null, null, null, null, null, null, null, null, '2025-01-01', '2025-01-01', '2025-02-01', 'Prison'),
    ('GAX911', 'WHI', 'C', null, null, null, null, 'POM, Primary', null, null, 123, null, null, null, '2025-01-01', '2025-01-01', '2025-02-01', 'Prison');