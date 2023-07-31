CREATE SCHEMA main;
CREATE SCHEMA notice;
CREATE SCHEMA course;

CREATE TABLE main.department (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP
);

CREATE TABLE main.users (
    id SERIAL PRIMARY KEY,
    student_id INT NOT NULL,
    name VARCHAR(32) NOT NULL,
    nickname VARCHAR(32) NULL,
    department_id INT NOT NULL,
    image_url VARCHAR(2048) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES main.department (id)
);

CREATE TABLE main.article (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(256) NOT NULL,
    content VARCHAR(2048) NOT NULL,
    image_url VARCHAR(2048) NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES main.users (id)
);

CREATE TABLE main.comment (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    article_id INT NOT NULL,
    content VARCHAR(512) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES main.users (id),
    FOREIGN KEY (article_id) REFERENCES main.article (id)
);

CREATE TABLE main.reaction (
    id SERIAL PRIMARY KEY,
    article_id INT NOT NULL,
    user_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (article_id) REFERENCES main.article (id),
    FOREIGN KEY (user_id) REFERENCES main.users (id)
);

CREATE TABLE main.scrap (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    article_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES main.users (id),
    FOREIGN KEY (article_id) REFERENCES main.article (id)
);

CREATE TABLE notice.notice (
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

CREATE TABLE notice.notice_fs (
    id SERIAL PRIMARY KEY,
    title VARCHAR(256) NOT NULL,
    content VARCHAR(2048) NOT NULL,
    image_url VARCHAR(2048) NULL,
    url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP
);

CREATE TABLE course.course (
    id SERIAL PRIMARY KEY,
    name VARCHAR(32) NOT NULL,
    term INT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP
);

CREATE TABLE course.video (
    id SERIAL PRIMARY KEY,
    course_id INT NOT NULL,
    due_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    start_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES course.course (id)
);

CREATE TABLE course.assignment (
    id SERIAL PRIMARY KEY,
    course_id INT NOT NULL,
    due_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    start_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (course_id) REFERENCES course.course (id)
);

CREATE TABLE course.user_course_relation (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    course_id INT NOT NULL,
    register_status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT current_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES main.users (id),
    FOREIGN KEY (course_id) REFERENCES course.course (id)
);
