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
DELETE FROM course_notice;
DELETE FROM service_notice;

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

INSERT INTO assignment (id, course_id, name, due_at, start_at, submit_at, detail, comments) VALUES
    (1, 1, '과제 1', '2024-01-09 12:00:00', '2023-01-01 00:00:00', null, '과제 첫번째 상세 내용', null);

INSERT INTO calendar (id, name, course_id, type, due_at, is_completed, user_id) VALUES
    (11, '이 날까지 과제 제출', 1, 'ASSIGNMENT', '2023-07-27 23:59:59', false, 1),
    (12, '이 날까지 강의 시청', 1, 'VIDEO', '2023-07-27 23:59:59', false, null),
    (13, '강의', 2, 'VIDEO', '2023-02-27 23:59:59', false, 1),
    (14, '강의', 4, 'VIDEO', '2023-05-31 23:59:59', false, null),
    (17, 'do it 과제', 2, 'VIDEO', CURRENT_DATE || ' 23:59:59', false, 1),
    (19, 'eat 과제1', 1, 'ASSIGNMENT', CURRENT_DATE || ' 09:00:00', false, 1),
    (20, 'eat 과제2', 1, 'ASSIGNMENT', CURRENT_DATE || ' 23:59:59', false, null),
    (21, 'just 과제1', 4, 'ASSIGNMENT', CURRENT_DATE || ' 16:00:00', false, 1),
    (22, 'just 과제2', 4, 'ASSIGNMENT', CURRENT_DATE || ' 23:59:59', false, null),
    (23, 'just 과제1', 3, 'ASSIGNMENT', CURRENT_DATE || ' 18:00:00', false, 1);

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

INSERT INTO course_notice(id, course_id, is_active, name, registered_at, views, content, file_url) VALUES
    (1, 1, true, '강의의 공지1', '2024-01-01 04:16:12', 3, '공지 상세 내용', '{"url": []}');

INSERT INTO service_notice(id, title, content, created_at, updated_at ) VALUES
    (1, '6/23 (금) 시스템 점검 및 업데이트 안내', '안녕하세요 다잇슈입니다. 더욱 쾌적하고 안정적인 서비스 지원을 위해 아래와 같이 점검이 진행됩니다.', '2023-12-06 06:44:07', '2023-12-06 06:44:07');
