DROP TABLE IF EXISTS recipes;

CREATE TABLE recipes
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(250) NOT NULL,
    description VARCHAR(255),
    ingredients TEXT         NOT NULL,
    imageUrl    VARCHAR(500),
    createdAt   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO recipes (title, description, ingredients, imageUrl)
VALUES ('Poulet Basquaise', 'Un plat traditionnel du Pays Basque.', 'Poulet, poivrons, tomates, oignons, ail',
        'https://pixabay.com/fr/photos/poulet-animal-la-volaille-cultiver-3741129/'),
       ('Ratatouille', 'Un ragoût de légumes provençal.', 'Courgettes, aubergines, poivrons, tomates, oignon, ail',
        'http://example.com/images/ratatouille.jpg'),
       ('Bœuf Bourguignon', 'Un ragoût de bœuf français classique.',
        'Bœuf, lardons, oignons, carottes, vin rouge, bouillon de bœuf',
        'http://example.com/images/boeuf-bourguignon.jpg');
