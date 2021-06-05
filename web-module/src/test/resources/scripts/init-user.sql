INSERT INTO user (uuid, last_name, first_name, patronymic, email, password, role_id)
VALUES (UUID(), 'LastName1', 'FirstName1', 'Patronymic1', 'Admin@email.com', 'Test password', 1);

INSERT INTO user (uuid, last_name, first_name, patronymic, email, password, role_id)
VALUES (UUID(), 'LastName2', 'FirstName2', 'Patronymic2', 'Seller@email.com', 'Test password', 2);

INSERT INTO user (uuid, last_name, first_name, patronymic, email, password, role_id)
VALUES (UUID(), 'LastName3', 'FirstName3', 'Patronymic3', 'Customer@email.com', 'Test password', 3);
INSERT INTO user_contacts (user_id, address, phone_number)
VALUES ((SELECT user.id FROM user WHERE last_name = 'LastName3'), 'Address', '+3751111111');

INSERT INTO user (uuid, last_name, first_name, patronymic, email, password, role_id)
VALUES (UUID(), 'LastName4', 'FirstName4', 'Patronymic4', 'Rest@email.com', 'Test password', 4);