INSERT INTO article(uuid, title, synopsis, addition_date_time, user_id)
VALUES (UUID(), 'Title1', 'Synopsis1', NOW(), (SELECT user.id FROM user WHERE last_name = 'LastName1'));

INSERT INTO article_content(article_id, content)
VALUES ((SELECT id FROM article WHERE title = 'Title1'),
        'Content1 Content1 Content1 Content1 Content1 Content1 Content1 Content1');

INSERT INTO article(uuid, title, synopsis, addition_date_time, user_id)
VALUES (UUID(), 'Title2', 'Synopsis2', NOW(), (SELECT user.id FROM user WHERE last_name = 'LastName2'));

INSERT INTO article_content(article_id, content)
VALUES ((SELECT id FROM article WHERE title = 'Title2'),
        'Content2 Content2 Content2 Content2 Content2 Content2 Content2 Content2');

INSERT INTO article(uuid, title, synopsis, addition_date_time, user_id)
VALUES (uuid(), 'Title3', 'Synopsis3', NOW(), (SELECT user.id FROM user WHERE last_name = 'LastName3'));

INSERT INTO article_content(article_id, content)
VALUES ((SELECT id FROM article WHERE title = 'Title3'),
        'Content3 Content3 Content3 Content3 Content3 Content3 Content3 Content3');

INSERT INTO article(uuid, title, synopsis, addition_date_time, user_id)
VALUES (UUID(), 'Title4', 'Synopsis4', NOW(), (SELECT user.id FROM user WHERE last_name = 'LastName4'));

INSERT INTO article_content(article_id, content)
VALUES ((SELECT id FROM article WHERE title = 'Title4'),
        'Content4 Content4 Content4 Content4 Content4 Content4 Content4 Content4');