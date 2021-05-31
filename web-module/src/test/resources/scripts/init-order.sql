INSERT INTO user_order(order_date_time, status, user_id)
VALUES (NOW(), 'NEW', (SELECT user.id FROM user WHERE last_name = 'LastName3'));

INSERT INTO order_detail (item_id, price, quantity, order_id)
VALUES ((SELECT item.id FROM item WHERE name = 'Item1'),
        (SELECT item.price FROM item WHERE name = 'Item1'),
        5,
        (SELECT user_order.id
         FROM user_order
         WHERE user_id = (SELECT user.id FROM user WHERE last_name = 'LastName3')));

INSERT INTO order_detail (item_id, price, quantity, order_id)
VALUES ((SELECT item.id FROM item WHERE name = 'Item2'),
        (SELECT item.price FROM item WHERE name = 'Item2'),
        5,
        (SELECT user_order.id
         FROM user_order
         WHERE user_id = (SELECT user.id FROM user WHERE last_name = 'LastName3')));