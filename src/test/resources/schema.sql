CREATE SCHEMA IF NOT EXISTS main;
CREATE SCHEMA IF NOT EXISTS notice;
CREATE SCHEMA IF NOT EXISTS course;

CREATE TABLE IF NOT EXISTS main.department (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS main.users (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL,
    name VARCHAR(32) NOT NULL,
    nickname VARCHAR(32) NULL,
    department_id INT NOT NULL,
    image_url VARCHAR(2048) NULL,
    term INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES main.department (id)
);

CREATE TABLE IF NOT EXISTS main.article (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(256) NOT NULL,
    content VARCHAR(2048) NOT NULL,
    image_url VARCHAR(2048) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES main.users (id)
);

CREATE TABLE IF NOT EXISTS main.comment (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    article_id INT NOT NULL,
    content VARCHAR(512) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES main.users (id),
    FOREIGN KEY (article_id) REFERENCES main.article (id)
);

CREATE TABLE IF NOT EXISTS main.reaction (
    id SERIAL PRIMARY KEY,
    article_id INT NOT NULL,
    user_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES main.article (id),
    FOREIGN KEY (user_id) REFERENCES main.users (id)
);

CREATE TABLE IF NOT EXISTS main.scrap (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    article_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES main.users (id),
    FOREIGN KEY (article_id) REFERENCES main.article (id)
);

CREATE TABLE IF NOT EXISTS notice.notice (
    id SERIAL PRIMARY KEY,
    title VARCHAR(1024) NOT NULL,
    department_id INT NOT NULL,
    content VARCHAR(2048) NOT NULL,
    category VARCHAR(32) NOT NULL,
    image_url VARCHAR(2048) NULL,
    file_url VARCHAR(2048) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS notice.notice_fs (
    id SERIAL PRIMARY KEY,
    title VARCHAR(256) NOT NULL,
    content VARCHAR(2048) NOT NULL,
    image_url VARCHAR(2048) NULL,
    url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS course.course (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
    term INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS course.video (
    id SERIAL PRIMARY KEY,
    course_id INT NOT NULL,
    due_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    start_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES course.course (id)
);

CREATE TABLE IF NOT EXISTS course.assignment (
    id SERIAL PRIMARY KEY,
    course_id INT NOT NULL,
    due_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    start_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES course.course (id)
);

CREATE TABLE IF NOT EXISTS course.user_course_relation (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    course_id INT NOT NULL,
    register_status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES main.users (id),
    FOREIGN KEY (course_id) REFERENCES course.course (id)
);

CREATE TABLE IF NOT EXISTS course.calendar (
    id SERIAL PRIMARY KEY,
    type VARCHAR(32) NOT NULL,
    course VARCHAR(32) NOT NULL,
    due_at TIMESTAMP NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_timestamp,
    updated_at TIMESTAMP NOT NULL DEFAULT current_timestamp
);
