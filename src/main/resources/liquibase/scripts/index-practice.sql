-- liquibase formatted sql

--changeset NShiv:1
CREATE INDEX student_name_index ON student (name);

CREATE INDEX faculty_name_and_color_index ON faculty (name,color);

