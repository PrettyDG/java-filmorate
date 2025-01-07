MERGE INTO MPA
(mpaID, mpa)
VALUES(1, 'G');

MERGE INTO MPA
(mpaID, mpa)
VALUES(2, 'PG');

MERGE INTO MPA
(mpaID, mpa)
VALUES(3, 'PG-13');

MERGE INTO MPA
(mpaID, mpa)
VALUES(4, 'R');

MERGE INTO MPA
(mpaID, mpa)
VALUES(5, 'NC-17');

MERGE INTO GENRE
(genre_id, genre)
VALUES(1, 'Комедия');

MERGE INTO GENRE
(genre_id, genre)
VALUES(2, 'Драма');

MERGE INTO GENRE
(genre_id, genre)
VALUES(3, 'Мультфильм');

MERGE INTO GENRE
(genre_id, genre)
VALUES(4, 'Триллер');

MERGE INTO GENRE
(genre_id, genre)
VALUES(5, 'Документальный');

MERGE INTO GENRE
(genre_id, genre)
VALUES(6, 'Боевик');

MERGE INTO friendshipStatus
(friendshipStatusID, friendshipStatus)
VALUES(1, 'PENDING');

MERGE INTO friendshipStatus
(friendshipStatusID, friendshipStatus)
VALUES(2, 'CONFIRMED');