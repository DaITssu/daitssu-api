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
