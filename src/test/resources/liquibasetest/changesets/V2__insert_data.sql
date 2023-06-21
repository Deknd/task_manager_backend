insert into users (name, username, password)
values ('John Doe', 'johndoe@mail.com', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK'),
       ('Mike Smith', 'mikesmith@yahoo.com', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK'),
       ('Diamantina Murat', 'PcdN1UpsE7@mail.ru', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK'),
       ('Nut Walter', 'madMvkCR1Yh@yahoo.com', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK'),
       ('Aruna Aytac', 'm57NMah@yahoo.com', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK'),
       ('Bláán Odessa', 'BlaanOdessa@yahoo.com', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK'),
       ('Bud Louis', 'bud_louis@yahoo.com', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK'),
       ('Janez Clifton', 'janez_clifton@yahoo.com', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK'),
       ('Albana Orfeo', 'albana_orfeo@yahoo.com', '$2a$10$GQ9L2PsLbLze56Is68/RqeNWs91E55DhsL569M0pzl9mCpZhntPzK');





insert into tasks(user_id, title, description, status, expiration_date, priority)
values ('2', 'Buy cheese', null, 'TODO', '2023-05-05 12:00:00', 'STANDARD'),
       ('2', 'Do homework', 'Math, Physics, Literature', 'IN_PROGRESS', '2023-05-06 00:00:00', 'STANDARD'),
       ('2', 'Clean rooms', null, 'DONE', null, 'STANDARD'),
       ('1', 'Call Mike', 'Ask about meeting', 'TODO', '2023-05-07 00:00:00', 'STANDARD'),
       ('8', 'Task 1', 'Description 1', 'TODO', '2023-07-08 12:00:00', 'STANDARD'),
       ('8', 'Task 2', 'Description 2', 'IN_PROGRESS', '2023-07-09 00:00:00', 'STANDARD'),
       ('8', 'Task 3', 'Description 3', 'DONE', null, 'STANDARD'),
       ('8', 'Task 4', 'Description 4', 'TODO', '2023-06-10 00:00:00', 'STANDARD');
insert into users_roles (user_id, role)
values (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER'),
       (3, 'ROLE_USER'),
       (4, 'ROLE_USER'),
       (5, 'ROLE_USER'),
       (6, 'ROLE_USER'),
       (7, 'ROLE_USER'),
       (8, 'ROLE_USER'),
       (9, 'ROLE_USER');