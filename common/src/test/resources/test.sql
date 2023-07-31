INSERT INTO main.department(name) VALUES
    ('computer'),
    ('media'),
    ('electronic');

INSERT INTO main.users(student_id, name, department_id) VALUES
    (1001, 'Alpha', 1),
    (2002, 'Bravo', 2),
    (3003, 'Charlie', 2),
    (4004, 'Delta', 3);

INSERT INTO course.course (name, term) VALUES
    ('eat paper', 15),
    ('do it', 15),
    ('im hungry', 14),
    ('choco', 16);

INSERT INTO course.user_course_relation(user_id, course_id, register_status) VALUES
    (1, 1, 'ACTIVE'),
    (1, 2, 'DROP'),
    (1, 3, 'ACTIVE'),
    (1, 4, 'ACTIVE'),
    (2, 3, 'ACTIVE');
