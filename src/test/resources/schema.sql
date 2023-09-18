CREATE SCHEMA IF NOT EXISTS notice;

CREATE TABLE IF NOT EXISTS notice.notice(
    id SERIAL PRIMARY KEY,
    title VARCHAR(1024) NOT NULL,
    department_id integer NOT NULL,
    content VARCHAR(2048) NOT NULL,
    category VARCHAR(32) NOT NULL,
    image_url VARCHAR(2048) NULL,
    file_url VARCHAR(2048) NULL,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL
);

CREATE TABLE IF NOT EXISTS notice.notice_fs(
    id SERIAL PRIMARY KEY,
    title VARCHAR(1024) NOT NULL,
    content VARCHAR(2048) NOT NULL,
    category VARCHAR(32) NOT NULL,
    image_url VARCHAR(2048) NULL,
    url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL
);

