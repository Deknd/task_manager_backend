insert into users (name, username, password)
values ('John Doe', 'johndoe@mail.com', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK'),
       ('Mike Smith', 'mikesmith@yahoo.com', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK');
insert into tasks(user_id, title, description, status, expiration_date, priority)
values ('2', 'Buy cheese', null, 'TODO', '2023-05-05 12:00:00', 'STANDART'),
       ('2', 'Do homework', 'Math, Physics, Literature', 'IN_PROGRESS', '2023-05-06 00:00:00', 'STANDART'),
       ('2', 'Clean rooms', null, 'DONE', null, 'STANDART'),
       ('1', 'Call Mike', 'Ask about meeting', 'TODO', '2023-05-07 00:00:00', 'STANDART');
insert into users_roles (user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');