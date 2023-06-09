BEGIN TRANSACTION;

DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS brewery;
DROP TABLE IF EXISTS beers;
DROP TABLE IF EXISTS reviews;

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    role VARCHAR(50) NOT NULL,
    username varchar(50) NOT NULL UNIQUE,
    password_hash varchar(200) NOT NULL,
    name VARCHAR(255) ,
    city VARCHAR(255),
    state VARCHAR(2),
    zip VARCHAR(10)
);


CREATE TABLE brewery (
    brewery_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    image TEXT,
    history TEXT,
	website TEXT,
    hours_days_operation VARCHAR(255),
    address VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES users (user_id)
);



CREATE TABLE beers (
    beer_id SERIAL PRIMARY KEY,
	brewery_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    abv DOUBLE PRECISION NOT NULL,
    beer_type VARCHAR(255) NOT NULL,
	FOREIGN KEY (brewery_id) REFERENCES brewery (brewery_id)

);


CREATE TABLE reviews (
    review_id SERIAL PRIMARY KEY,
    user_id INT,
    brewery_id INT,
    beer_id INT,
    text TEXT,
    date DATE NOT NULL,
    rating INT NOT NULL,
	is_for_beers BOOLEAN NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (user_id),
    FOREIGN KEY (brewery_id) REFERENCES brewery (brewery_id),
    FOREIGN KEY (beer_id) REFERENCES beers (beer_id)
);


COMMIT TRANSACTION;
