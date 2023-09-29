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

CREATE TABLE objective
(
    id          SERIAL PRIMARY KEY,
    resume_uuid CHAR(36) NOT NULL,
    value       TEXT        NOT NULL,
    FOREIGN KEY (resume_uuid) REFERENCES resume (uuid)
);

CREATE UNIQUE INDEX contact_uuid_type_index
    ON contact USING btree
    (resume_uuid ASC NULLS LAST, type ASC NULLS LAST);