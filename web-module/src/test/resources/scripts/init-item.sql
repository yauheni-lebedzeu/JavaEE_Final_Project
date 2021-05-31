INSERT INTO item(uuid, name, price, quantity) VALUES (UUID(), 'Item1', 22.50, 50);
INSERT INTO item_description(item_id, description) VALUES ((SELECT id FROM item WHERE name = 'Item1'), 'Description1');

INSERT INTO item(uuid, name, price, quantity) VALUES (UUID(), 'Item2', 1000, 87);
INSERT INTO item_description(item_id, description) VALUES ((SELECT id FROM item WHERE name = 'Item2'), 'Description2');

INSERT INTO item(uuid, name, price, quantity) VALUES (UUID(), 'Item3', 138.01, 12);
INSERT INTO item_description(item_id, description) VALUES ((SELECT id FROM item WHERE name = 'Item3'), 'Description3');

INSERT INTO item(uuid, name, price, quantity) VALUES (UUID(), 'Item4', 545, 99);
INSERT INTO item_description(item_id, description) VALUES ((SELECT id FROM item WHERE name = 'Item4'), 'Description4');