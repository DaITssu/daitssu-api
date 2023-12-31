DELETE FROM user_course_relation;
DELETE FROM video;
DELETE FROM calendar;
DELETE FROM assignment;
DELETE FROM course;
DELETE FROM comment;
DELETE FROM scrap;
DELETE FROM article;
DELETE FROM users;
DELETE FROM department;
DELETE FROM notice;
DELETE FROM notice_fs;

INSERT INTO department(id, name) VALUES
    (1, 'computer'),
    (2, 'media'),
    (3, 'electronic');

INSERT INTO users(id, student_id, name, nickname, department_id, term, is_deleted) VALUES
    (1, 1001, 'Alpha', null, 1, 1, false),
    (2, 2002, 'Bravo', 'B', 2, 2, false),
    (3, 3003, 'Charlie', null, 2, 2, false),
    (4, 4004, 'Delta', 'D', 3, 3, false);

INSERT INTO article(id, topic, title, content, user_id, image_url, created_at, updated_at) VALUES
    (1, 'CHAT', '대충 제목1', '대충 내용1', 2, '{"url": []}', '2023-09-16 10:00:00.000', '2023-09-16 10:00:00.000'),
    (2, 'QUESTION', '대충 제목2', '대충 내용1', 4, '{"url": []}', '2023-09-16 10:00:00.000', '2023-09-16 10:00:00.000'),
    (3, 'INFORMATION', '대충 제목3', '대충 내용1', 4, '{"url": []}', '2023-09-16 10:00:00.000', '2023-09-16 10:00:00.000');

INSERT INTO comment(id, user_id, article_id, content, original_id, is_deleted, created_at, updated_at) VALUES
    (1, 1, 1, '대충 댓글 쓴거1', null, false, '2023-09-16 10:00:00.000', '2023-09-16 10:00:00.000'),
    (2, 3, 1, '대충 대댓글 쓴거1', 1, false, '2023-09-16 10:00:00.000', '2023-09-16 10:00:00.000'),
    (3, 1, 3, '대충 댓글 쓴거2', null, false, '2023-09-16 10:00:00.000', '2023-09-16 10:00:00.000'),
    (4, 1, 3, '대충 댓글 쓴거3', null, true, '2023-09-16 10:00:00.000', '2023-09-16 10:00:00.000');

INSERT INTO scrap(id, user_id, article_id, is_active, created_at, updated_at) VALUES
    (1, 1, 1, true, '2023-09-16 10:00:00.000', '2023-09-16 10:00:00.000'),
    (2, 3, 1, false, '2023-09-16 10:00:00.000', '2023-09-16 10:00:00.000');

INSERT INTO course (id, name, term, course_code, created_at, updated_at) VALUES
    (1, 'eat paper', 15, '24123', '2023-07-27 10:00:00.000', '2023-07-27 10:00:00.000'),
    (2, 'do it', 15, '24124', '2023-07-28 10:00:00.000', '2023-07-28 10:00:00.000'),
    (3, 'im hungry', 14, '24125', '2023-07-29 10:00:00.000', '2023-07-29 10:00:00.000'),
    (4, 'choco', 16, '24126', '2023-07-30 10:00:00.000', '2023-07-30 10:00:00.000');

INSERT INTO user_course_relation(id, user_id, course_id, register_status) VALUES
    (1, 1, 1, 'ACTIVE'),
    (2, 2, 2, 'DROP'),
    (3, 3, 3, 'ACTIVE'),
    (4, 1, 4, 'ACTIVE'),
    (5, 2, 3, 'ACTIVE');

INSERT INTO video (id, course_id, name, start_at, due_at) VALUES
    (1, 1, '1강', '2023-07-20 12:00:00', '2023-07-27 23:59:59'),
    (2, 1, '2강', '2023-08-20 12:00:00', '2023-08-27 23:59:59'),
    (3, 2, '첫 번째 강의', '2023-07-20 12:00:00', '2023-07-27 23:59:59'),
    (4, 4, '2번째 강의', '2023-08-20 12:00:00', '2023-08-27 23:59:59');

INSERT INTO assignment (id, course_id, name, start_at, due_at) VALUES
    (1, 1, '첫 번째 숙제', '2023-07-20 12:00:00', '2023-07-27 23:59:59'),
    (2, 1, '두 번째 숙제', '2023-08-20 12:00:00', '2023-08-27 23:59:59'),
    (3, 2, '1-1', '2023-07-20 12:00:00', '2023-07-27 23:59:59'),
    (4, 3, '2-1', '2023-08-20 12:00:00', '2023-08-27 23:59:59');

INSERT INTO calendar (id, name, course, type, due_at, is_complete) VALUES
    (11, '이 날까지 과제 제출', 'eat paper', 'ASSIGNMENT', '2023-07-27 23:59:59', false),
    (12, '이 날까지 강의 시청', 'eat paper', 'VIDEO', '2023-07-27 23:59:59', false),
    (13, '강의', 'do it', 'VIDEO', '2023-02-27 23:59:59', false),
    (14, '강의', 'choco', 'VIDEO', '2023-05-31 23:59:59', false),
    (15, 'eat 과제1', 'eat paper', 'VIDEO', CURRENT_DATE || ' 16:00:00', false),
    (16, 'eat 과제2', 'eat paper', 'VIDEO', CURRENT_DATE || ' 23:59:59', false),
    (17, 'do it 과제', 'do it', 'VIDEO', CURRENT_DATE || ' 23:59:59', false),
    (18, 'choco 과제', 'choco', 'VIDEO', CURRENT_DATE || ' 18:00:00', false),
    (19, 'eat 과제1', 'eat paper', 'ASSIGNMENT', CURRENT_DATE || ' 09:00:00', false),
    (20, 'eat 과제2', 'eat paper', 'ASSIGNMENT', CURRENT_DATE || ' 23:59:59', false),
    (21, 'just 과제1', 'just', 'ASSIGNMENT', CURRENT_DATE || ' 16:00:00', false),
    (22, 'just 과제2', 'just', 'ASSIGNMENT', CURRENT_DATE || ' 23:59:59', false),
    (23, 'just 과제1', 'good', 'ASSIGNMENT', CURRENT_DATE || ' 18:00:00', false);

INSERT INTO notice(id, title, department_id, content, category, image_url, file_url, created_at, updated_at, views) VALUES
    (1,'공지사항1',1,'1번 공지 내용입니다!!','ACADEMICS', '{"url": []}', '{"url": []}', '1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (2,'공지사항2',2,'2번 공지 내용입니다!!','SUBSCRIPTION', '{"url": []}', '{"url": []}', '1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (3,'공지사항3',3,'3번 공지 내용입니다!!','SCHOLARSHIP', '{"url": []}', '{"url": []}', '1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (4,'공지사항4',4,'4번 공지 내용입니다!!','INTERNATIONAL_EXCHANGE', '{"url": []}', '{"url": []}', '1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (5,'공지사항5',5,'5번 공지 내용입니다!!','INTERNATIONAL_EXCHANGE', '{"url": []}', '{"url": []}', '1000-01-01 00:00:00','1000-01-01 00:00:00',0);

INSERT INTO notice_fs(id, title, content, category,url, image_url, created_at, updated_at,views) VALUES
    (1,'공지사항1','1번 공지 내용입니다!!','CERTIFICATION','http://google.com', '{"url": []}', '1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (2,'공지사항2','2번 공지 내용입니다!!','SUBSCRIPTION','http://google.com', '{"url": []}', '1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (3,'공지사항3','3번 공지 내용입니다!!','CERTIFICATION','http://google.com', '{"url": []}', '1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (4,'공지사항4','4번 공지 내용입니다!!','EXPERIENTIAL_ACTIVITIES','http://google.com', '{"url": []}', '1000-01-01 00:00:00','1000-01-01 00:00:00',0),
    (5,'공지사항5','5번 공지 내용입니다!!','EXPERIENTIAL_ACTIVITIES','http://google.com', '{"url": []}', '1000-01-01 00:00:00','1000-01-01 00:00:00',0);
