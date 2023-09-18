DELETE FROM course.user_course_relation;
DELETE FROM course.course;
DELETE FROM main.users;
DELETE FROM main.department;

INSERT INTO main.department(id, name) VALUES
    (1, 'computer'),
    (2, 'media'),
    (3, 'electronic');

INSERT INTO main.users(id, student_id, name, nickname, department_id, term) VALUES
    (1, 1001, 'Alpha', null, 1, 1),
    (2, 2002, 'Bravo', 'B', 2, 2),
    (3, 3003, 'Charlie', null, 2, 2),
    (4, 4004, 'Delta', 'D', 3, 3);

INSERT INTO course.course (id, name, term, created_at, updated_at) VALUES
    (1, 'eat paper', 15, '2023-07-27 10:00:00.000', '2023-07-27 10:00:00.000'),
    (2, 'do it', 15, '2023-07-28 10:00:00.000', '2023-07-28 10:00:00.000'),
    (3, 'im hungry', 14, '2023-07-29 10:00:00.000', '2023-07-29 10:00:00.000'),
    (4, 'choco', 16, '2023-07-30 10:00:00.000', '2023-07-30 10:00:00.000');

INSERT INTO course.user_course_relation(id, user_id, course_id, register_status) VALUES
    (1, 1, 1, 'ACTIVE'),
    (2, 2, 2, 'DROP'),
    (3, 3, 3, 'ACTIVE'),
    (4, 1, 4, 'ACTIVE'),
    (5, 2, 3, 'ACTIVE');

INSERT INTO course.video (id, course_id, name, start_at, due_at) VALUES
    (1, 1, '1강', '2023-07-20 12:00:00', '2023-07-27 23:59:59'),
    (2, 1, '2강', '2023-08-20 12:00:00', '2023-08-27 23:59:59'),
    (3, 2, '첫 번째 강의', '2023-07-20 12:00:00', '2023-07-27 23:59:59'),
    (4, 4, '2번째 강의', '2023-08-20 12:00:00', '2023-08-27 23:59:59');

INSERT INTO course.assignment (id, course_id, name, start_at, due_at) VALUES
    (1, 1, '첫 번째 숙제', '2023-07-20 12:00:00', '2023-07-27 23:59:59'),
    (2, 1, '두 번째 숙제', '2023-08-20 12:00:00', '2023-08-27 23:59:59'),
    (3, 2, '1-1', '2023-07-20 12:00:00', '2023-07-27 23:59:59'),
    (4, 3, '2-1', '2023-08-20 12:00:00', '2023-08-27 23:59:59');

INSERT INTO course.calendar (id, name, course, type, due_at, is_completed) VALUES
    (11, '이 날까지 과제 제출', 'eat paper', 'ASSIGNMENT', '2023-07-27 23:59:59', false),
    (12, '이 날까지 강의 시청', 'eat paper', 'VIDEO', '2023-07-27 23:59:59', false),
    (13, '강의', 'do it', 'VIDEO', '2023-02-27 23:59:59', false),
    (14, '강의', 'choco', 'VIDEO', '2023-05-31 23:59:59', false);
