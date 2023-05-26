-- table.sql

-- mysql 오류 방지를 위해 user -> users로 변경
CREATE TABLE users
(
    user_id    BIGINT       NOT NULL AUTO_INCREMENT,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    email      VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    username   VARCHAR(255) NOT NULL,
    user_role VARCHAR(255) NOT NULL,
    CONSTRAINT user_pk
        PRIMARY KEY (user_id)
);

CREATE TABLE article
(
    article_id BIGINT       NOT NULL AUTO_INCREMENT,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    content    VARCHAR(255) NOT NULL,
    title      VARCHAR(255) NOT NULL,
    user_id    BIGINT       NOT NULL,
    PRIMARY KEY (article_id),
    FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE comment
(
    comment_id BIGINT       NOT NULL AUTO_INCREMENT,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    content    VARCHAR(255) NOT NULL,
    article_id BIGINT       NOT NULL,
    user_id    BIGINT       NOT NULL,
    PRIMARY KEY (comment_id),
    FOREIGN KEY (user_id)
        REFERENCES users (user_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (article_id)
        REFERENCES article (article_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);