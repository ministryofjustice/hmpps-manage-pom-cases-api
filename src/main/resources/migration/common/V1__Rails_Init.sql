CREATE SEQUENCE IF NOT EXISTS allocation_history_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS calculated_handover_dates_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS case_information_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS delius_import_errors_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS early_allocations_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS email_histories_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS handover_progress_checklists_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS local_delivery_units_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS offender_email_opt_outs_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS offender_email_sent_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS parole_review_imports_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS parole_reviews_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS pom_details_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS responsibilities_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS versions_id_seq START WITH 1 INCREMENT BY 1;

CREATE SEQUENCE IF NOT EXISTS victim_liaison_officers_id_seq START WITH 1 INCREMENT BY 1;


CREATE TYPE offender_email_type AS ENUM (
    'upcoming_handover_window',
    'handover_date',
    'com_allocation_overdue'
);

CREATE TABLE allocation_history
(
    id                       BIGINT NOT NULL DEFAULT nextval('allocation_history_id_seq'),
    nomis_offender_id        VARCHAR,
    prison                   VARCHAR,
    allocated_at_tier        VARCHAR,
    override_reasons         VARCHAR,
    override_detail          VARCHAR,
    message                  VARCHAR,
    suitability_detail       VARCHAR,
    primary_pom_name         VARCHAR,
    secondary_pom_name       VARCHAR,
    created_by_name          VARCHAR,
    primary_pom_nomis_id     INTEGER,
    secondary_pom_nomis_id   INTEGER,
    event                    INTEGER,
    event_trigger            INTEGER,
    created_at               TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    primary_pom_allocated_at TIMESTAMP WITH TIME ZONE,
    recommended_pom_type     VARCHAR,
    CONSTRAINT allocation_history_pkey PRIMARY KEY (id)
);

CREATE TABLE audit_events
(
    id                CHAR(36) DEFAULT gen_random_uuid() NOT NULL,
    nomis_offender_id TEXT,
    tags              TEXT ARRAY                         NOT NULL,
    published_at      TIMESTAMP WITH TIME ZONE           NOT NULL,
    system_event      BOOLEAN,
    username          TEXT,
    user_human_name   TEXT,
    data              JSONB                              NOT NULL,
    created_at        TIMESTAMP WITH TIME ZONE           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT system_event_cannot_have_user_details CHECK ((((system_event = true) AND (username IS NULL) AND (user_human_name IS NULL)) OR (system_event = false))),
    CONSTRAINT audit_events_pkey PRIMARY KEY (id)
);

CREATE TABLE calculated_early_allocation_statuses
(
    nomis_offender_id VARCHAR NOT NULL,
    eligible          BOOLEAN NOT NULL,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT calculated_early_allocation_statuses_pkey PRIMARY KEY (nomis_offender_id)
);

CREATE TABLE calculated_handover_dates
(
    id                 BIGINT NOT NULL DEFAULT nextval('calculated_handover_dates_id_seq'),
    start_date         date,
    handover_date      date,
    reason             VARCHAR                                 NOT NULL,
    created_at         TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nomis_offender_id  VARCHAR                                 NOT NULL,
    responsibility     VARCHAR,
    last_calculated_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT calculated_handover_dates_pkey PRIMARY KEY (id)
);

CREATE TABLE case_information
(
    id                     BIGINT NOT NULL DEFAULT nextval('case_information_id_seq'),
    tier                   VARCHAR,
    nomis_offender_id      VARCHAR,
    crn                    VARCHAR,
    mappa_level            INTEGER,
    manual_entry           BOOLEAN                                 NOT NULL,
    created_at             TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at             TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    probation_service      VARCHAR,
    com_name               VARCHAR,
    team_name              VARCHAR,
    local_delivery_unit_id BIGINT,
    ldu_code               VARCHAR,
    com_email              VARCHAR,
    active_vlo             BOOLEAN DEFAULT FALSE,
    enhanced_resourcing    BOOLEAN,
    CONSTRAINT case_information_pkey PRIMARY KEY (id)
);

CREATE TABLE delius_import_errors
(
    id                BIGINT NOT NULL DEFAULT nextval('delius_import_errors_id_seq'),
    nomis_offender_id VARCHAR,
    error_type        INTEGER,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT delius_import_errors_pkey PRIMARY KEY (id)
);

CREATE TABLE early_allocations
(
    id                                    BIGINT NOT NULL DEFAULT nextval('early_allocations_id_seq'),
    nomis_offender_id                     VARCHAR                                 NOT NULL,
    oasys_risk_assessment_date            date                                    NOT NULL,
    convicted_under_terrorisom_act_2000   BOOLEAN                                 NOT NULL,
    high_profile                          BOOLEAN                                 NOT NULL,
    serious_crime_prevention_order        BOOLEAN                                 NOT NULL,
    mappa_level_3                         BOOLEAN                                 NOT NULL,
    cppc_case                             BOOLEAN                                 NOT NULL,
    high_risk_of_serious_harm             BOOLEAN,
    mappa_level_2                         BOOLEAN,
    pathfinder_process                    BOOLEAN,
    other_reason                          BOOLEAN,
    extremism_separation                  BOOLEAN,
    due_for_release_in_less_than_24months BOOLEAN,
    approved                              BOOLEAN,
    reason                                VARCHAR,
    created_at                            TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                            TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    community_decision                    BOOLEAN,
    prison                                VARCHAR,
    created_by_firstname                  VARCHAR,
    created_by_lastname                   VARCHAR,
    updated_by_firstname                  VARCHAR,
    updated_by_lastname                   VARCHAR,
    created_within_referral_window        BOOLEAN DEFAULT FALSE                   NOT NULL,
    outcome                               VARCHAR                                 NOT NULL,
    CONSTRAINT early_allocations_pkey PRIMARY KEY (id)
);

CREATE TABLE email_histories
(
    id                BIGINT NOT NULL DEFAULT nextval('email_histories_id_seq'),
    prison            VARCHAR                                 NOT NULL,
    nomis_offender_id VARCHAR                                 NOT NULL,
    name              VARCHAR                                 NOT NULL,
    email             VARCHAR                                 NOT NULL,
    event             VARCHAR                                 NOT NULL,
    created_at        TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT email_histories_pkey PRIMARY KEY (id)
);

CREATE TABLE handover_progress_checklists
(
    id                        BIGINT NOT NULL DEFAULT nextval('handover_progress_checklists_id_seq'),
    nomis_offender_id         VARCHAR                                 NOT NULL,
    reviewed_oasys            BOOLEAN DEFAULT FALSE                   NOT NULL,
    contacted_com             BOOLEAN DEFAULT FALSE                   NOT NULL,
    attended_handover_meeting BOOLEAN DEFAULT FALSE                   NOT NULL,
    created_at                TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_handover_report      BOOLEAN DEFAULT FALSE                   NOT NULL,
    CONSTRAINT handover_progress_checklists_pkey PRIMARY KEY (id)
);

CREATE TABLE local_delivery_units
(
    id            BIGINT NOT NULL DEFAULT nextval('local_delivery_units_id_seq'),
    code          VARCHAR                                 NOT NULL,
    name          VARCHAR                                 NOT NULL,
    email_address VARCHAR                                 NOT NULL,
    country       VARCHAR                                 NOT NULL,
    enabled       BOOLEAN                                 NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT local_delivery_units_pkey PRIMARY KEY (id)
);

CREATE TABLE offender_email_opt_outs
(
    id                  BIGINT NOT NULL DEFAULT nextval('offender_email_opt_outs_id_seq'),
    staff_member_id     VARCHAR                                 NOT NULL,
    offender_email_type OFFENDER_EMAIL_TYPE                     NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT offender_email_opt_outs_pkey PRIMARY KEY (id)
);

CREATE TABLE offender_email_sent
(
    id                  BIGINT NOT NULL DEFAULT nextval('offender_email_sent_id_seq'),
    nomis_offender_id   VARCHAR                                 NOT NULL,
    staff_member_id     VARCHAR                                 NOT NULL,
    offender_email_type OFFENDER_EMAIL_TYPE                     NOT NULL,
    created_at          TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT offender_email_sent_pkey PRIMARY KEY (id)
);

CREATE TABLE offenders
(
    nomis_offender_id VARCHAR NOT NULL,
    created_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT offenders_pkey PRIMARY KEY (nomis_offender_id)
);

CREATE TABLE parole_review_imports
(
    id                       BIGINT NOT NULL DEFAULT nextval('parole_review_imports_id_seq'),
    title                    VARCHAR,
    nomis_id                 VARCHAR,
    prison_no                VARCHAR,
    sentence_type            VARCHAR,
    sentence_date            VARCHAR,
    tariff_exp               VARCHAR,
    review_date              VARCHAR,
    review_id                VARCHAR,
    review_milestone_date_id VARCHAR,
    review_type              VARCHAR,
    review_status            VARCHAR,
    curr_target_date         VARCHAR,
    ms13_target_date         VARCHAR,
    ms13_completion_date     VARCHAR,
    final_result             VARCHAR,
    snapshot_date            date,
    row_number               INTEGER,
    import_id                VARCHAR,
    single_day_snapshot      BOOLEAN,
    processed_on             date,
    created_at               TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at               TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT parole_review_imports_pkey PRIMARY KEY (id)
);

CREATE TABLE parole_reviews
(
    id                          BIGINT NOT NULL DEFAULT nextval('parole_reviews_id_seq'),
    review_id                   INTEGER,
    nomis_offender_id           VARCHAR,
    target_hearing_date         date,
    custody_report_due          date,
    review_status               VARCHAR,
    hearing_outcome             VARCHAR,
    hearing_outcome_received_on date,
    created_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    review_type                 VARCHAR,
    CONSTRAINT parole_reviews_pkey PRIMARY KEY (id)
);

CREATE TABLE pom_details
(
    id              BIGINT NOT NULL DEFAULT nextval('pom_details_id_seq'),
    nomis_staff_id  INTEGER,
    working_pattern DOUBLE PRECISION,
    status          VARCHAR,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    prison_code     VARCHAR,
    CONSTRAINT pom_details_pkey PRIMARY KEY (id)
);

CREATE TABLE prisons
(
    code        VARCHAR NOT NULL,
    prison_type VARCHAR NOT NULL,
    name        VARCHAR NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT prisons_pkey PRIMARY KEY (code)
);

CREATE TABLE responsibilities
(
    id                BIGINT NOT NULL DEFAULT nextval('responsibilities_id_seq'),
    nomis_offender_id VARCHAR                                 NOT NULL,
    reason            INTEGER                                 NOT NULL,
    reason_text       VARCHAR,
    "value"           VARCHAR                                 NOT NULL,
    created_at        TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT responsibilities_pkey PRIMARY KEY (id)
);

CREATE TABLE versions
(
    id                             BIGINT NOT NULL DEFAULT nextval('versions_id_seq'),
    item_type                      VARCHAR                                 NOT NULL,
    item_id                        BIGINT                                  NOT NULL,
    event                          VARCHAR                                 NOT NULL,
    whodunnit                      VARCHAR,
    object                         TEXT,
    created_at                     TIMESTAMP WITH TIME ZONE,
    object_changes                 TEXT,
    nomis_offender_id              VARCHAR,
    user_first_name                VARCHAR,
    user_last_name                 VARCHAR,
    prison                         VARCHAR,
    offender_attributes_to_archive JSONB,
    system_admin_note              VARCHAR,
    CONSTRAINT versions_pkey PRIMARY KEY (id)
);

CREATE TABLE victim_liaison_officers
(
    id                BIGINT NOT NULL DEFAULT nextval('victim_liaison_officers_id_seq'),
    first_name        VARCHAR                                 NOT NULL,
    last_name         VARCHAR                                 NOT NULL,
    email             VARCHAR                                 NOT NULL,
    created_at        TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    nomis_offender_id VARCHAR(7)                              NOT NULL,
    CONSTRAINT victim_liaison_officers_pkey PRIMARY KEY (id)
);

CREATE UNIQUE INDEX index_allocation_history_on_nomis_offender_id ON allocation_history (nomis_offender_id);

CREATE INDEX index_allocation_history_on_primary_pom_nomis_id ON allocation_history (primary_pom_nomis_id);

CREATE INDEX index_allocation_history_on_prison ON allocation_history (prison);

CREATE INDEX index_allocation_versions_secondary_pom_nomis_id ON allocation_history (secondary_pom_nomis_id);

CREATE UNIQUE INDEX index_calculated_handover_dates_on_nomis_offender_id ON calculated_handover_dates (nomis_offender_id);

CREATE INDEX index_case_information_on_local_delivery_unit_id ON case_information (local_delivery_unit_id);

CREATE UNIQUE INDEX index_case_information_on_nomis_offender_id ON case_information (nomis_offender_id);

CREATE UNIQUE INDEX index_local_delivery_units_on_code ON local_delivery_units (code);

CREATE UNIQUE INDEX index_offender_email_opt_out_unique_composite_key ON offender_email_opt_outs (staff_member_id, offender_email_type);

CREATE UNIQUE INDEX index_offender_email_sent_unique_composite_key ON offender_email_sent (nomis_offender_id, staff_member_id, offender_email_type);

CREATE INDEX index_parole_review_imports_on_processed_on ON parole_review_imports (processed_on);

CREATE UNIQUE INDEX index_parole_review_imports_on_snapshot_date_row_number ON parole_review_imports (snapshot_date, row_number);

CREATE UNIQUE INDEX index_parole_reviews_on_review_id_nomis_offender_id ON parole_reviews (review_id, nomis_offender_id);

CREATE UNIQUE INDEX index_pom_details_on_nomis_staff_id_and_prison_code ON pom_details (nomis_staff_id, prison_code);

CREATE UNIQUE INDEX index_prisons_on_name ON prisons (name);

CREATE INDEX index_versions_on_item_type_and_item_id ON versions (item_type, item_id);

CREATE INDEX index_versions_on_nomis_offender_id ON versions (nomis_offender_id);

CREATE INDEX index_victim_liaison_officers_on_nomis_offender_id ON victim_liaison_officers (nomis_offender_id);

ALTER TABLE handover_progress_checklists
    ADD CONSTRAINT fk_rails_0f7d3e1f9a FOREIGN KEY (nomis_offender_id) REFERENCES offenders (nomis_offender_id) ON DELETE NO ACTION;

CREATE UNIQUE INDEX index_handover_progress_checklists_on_nomis_offender_id ON handover_progress_checklists (nomis_offender_id);

ALTER TABLE offender_email_sent
    ADD CONSTRAINT fk_rails_5f6304c3c6 FOREIGN KEY (nomis_offender_id) REFERENCES offenders (nomis_offender_id) ON DELETE NO ACTION;

CREATE INDEX index_offender_email_sent_on_nomis_offender_id ON offender_email_sent (nomis_offender_id);
