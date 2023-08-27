DELETE FROM main.department;
DELETE FROM main.users;
DELETE FROM course.course;
DELETE FROM course.user_course_relation;

INSERT INTO main.department(id, name) VALUES
    (1, 'computer'),
    (2, 'media'),
    (3, 'electronic');

INSERT INTO main.users(id, student_id, name, department_id, term) VALUES
    (1, 1001, 'Alpha', 1, 1),
    (2, 2002, 'Bravo', 2, 2),
    (3, 3003, 'Charlie', 2, 2),
    (4, 4004, 'Delta', 3, 3);

INSERT INTO course.course (id, name, term) VALUES
    (1, 'eat paper', 15),
    (2, 'do it', 15),
    (3, 'im hungry', 14),
    (4, 'choco', 16);

INSERT INTO course.user_course_relation(id, user_id, course_id, register_status) VALUES
    (1, 1, 1, 'ACTIVE'),
    (2, 1, 2, 'DROP'),
    (3, 1, 3, 'ACTIVE'),
    (4, 1, 4, 'ACTIVE'),
    (5, 2, 3, 'ACTIVE');

INSERT INTO course.video (id, course_id, name, start_at, due_at) VALUES
    (1, 1, '1강', '2023-07-20T12:00:00', '2023-07-27T23:59:59'),
    (2, 1, '2강', '2023-08-20T12:00:00', '2023-08-27T23:59:59'),
    (3, 2, '첫 번째 강의', '2023-07-20T12:00:00', '2023-07-27T23:59:59'),
    (4, 4, '2번째 강의', '2023-08-20T12:00:00', '2023-08-27T23:59:59');

INSERT INTO course.assignment (id, course_id, name, start_at, due_at) VALUES
    (1, 1, '첫 번째 숙제', '2023-07-20T12:00:00', '2023-07-27T23:59:59'),
    (2, 1, '두 번째 숙제', '2023-08-20T12:00:00', '2023-08-27T23:59:59'),
    (3, 2, '1-1', '2023-07-20T12:00:00', '2023-07-27T23:59:59'),
    (4, 3, '2-1', '2023-08-20T12:00:00', '2023-08-27T23:59:59');

INSERT INTO course.calendar (id, name, course, type, due_at) VALUES
    (11, '이 날까지 과제 제출', 'eat paper', 'ASSIGNMENT', '2023-07-27T23:59:59'),
    (12, '이 날까지 강의 시청', 'eat paper', 'VIDEO', '2023-07-27T23:59:59'),
    (13, '강의', 'do it', 'VIDEO', '2023-02-27T23:59:59'),
    (14, '강의', 'choco', 'VIDEO', '2023-05-31T23:59:59');