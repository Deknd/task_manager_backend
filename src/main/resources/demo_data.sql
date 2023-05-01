insert into users (name, username, password)
values ('John Doe', 'johndoe@mail.com', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK'),
       ('Mike Smith', 'mikesmith@yahoo.com', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK');
insert into tasks(title, description, status, expiration_date)
values ('Buy cheese', null, 'TODO', '2023-05-05 12:00:00'),
       ('Do homework', 'Math, Physics, Literature', 'IN_PROGRESS', '2023-05-06 00:00:00'),
       ('Clean rooms', null, 'DONE', null),
       ('Call Mike', 'Ask about meeting', 'TODO', '2023-05-07 00:00:00');
insert into users_task(task_id, user_id)
values (1, 2),
       (2, 2),
       (3, 2),
       (4, 1);
insert into users_roles (user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');