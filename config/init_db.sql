CREATE TABLE resume
(
    uuid      CHAR(36) PRIMARY KEY NOT NULL,
    full_name TEXT                 NOT NULL
);

CREATE TABLE contact
(
    id          SERIAL PRIMARY KEY,
    resume_uuid CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    type        TEXT     NOT NULL,
    value       TEXT     NOT NULL
);

CREATE TABLE section
(
    id_section    SERIAL PRIMARY KEY,
    resume_uuid   CHAR(36) NOT NULL REFERENCES resume (uuid) ON DELETE CASCADE,
    objective     TEXT,
    personal      TEXT,
    achievement   TEXT,
    qualification TEXT
);

CREATE UNIQUE INDEX contact_uuid_type_index
    ON contact USING btree
    (resume_uuid ASC NULLS LAST, type ASC NULLS LAST);