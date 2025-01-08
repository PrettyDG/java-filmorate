CREATE TABLE IF NOT EXISTS MPA (
    mpaID INTEGER PRIMARY KEY,
    mpa VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS FILM (
    film_id INTEGER PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    releaseDate DATE,
    duration INTEGER,
    mpaID INTEGER,
    FOREIGN KEY (mpaID) REFERENCES MPA(mpaID)
);

CREATE TABLE IF NOT EXISTS GENRE (
    genre_id INTEGER PRIMARY KEY,
    genre VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS FILM_GENRE (
    film_id INTEGER,
    genre_id INTEGER,
    FOREIGN KEY (film_id) REFERENCES FILM(film_id),
    FOREIGN KEY (genre_id) REFERENCES GENRE(genre_id),
    PRIMARY KEY (film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS USERS (
    id INTEGER PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS filmLikedByUsers (
    film_id INTEGER,
    userID INTEGER,
    FOREIGN KEY (film_id) REFERENCES FILM(film_id),
    FOREIGN KEY (userID) REFERENCES USERS(id),
    PRIMARY KEY (film_id, userID)
);

CREATE TABLE IF NOT EXISTS friendshipStatus (
    friendshipStatusID INTEGER PRIMARY KEY,
    friendshipStatus VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
    userID INTEGER,
    friendID INTEGER,
    friendshipStatusID INTEGER,
    FOREIGN KEY (userID) REFERENCES USERS(id),
    FOREIGN KEY (friendID) REFERENCES USERS(id),
    FOREIGN KEY (friendshipStatusID) REFERENCES friendshipStatus(friendshipStatusID),
    PRIMARY KEY (userID, friendID)
);
