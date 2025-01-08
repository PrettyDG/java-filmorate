package ru.yandex.practicum.filmorate.storage.film.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.storage.film.mappers.FilmGenresMapper;

import java.util.List;
import java.util.Set;

@Repository("filmGenresDbStorage")
@Slf4j
public class FilmGenresDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmGenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<FilmGenre> findAllGenresByFilmId(Long filmId) {
        String sql = "SELECT * FROM FILM_GENRE WHERE film_id = ?";
        List<FilmGenre> filmGenres = jdbcTemplate.query(sql, new FilmGenresMapper(), filmId);
        log.info("Получен запрос на получения всех жанров фильма с id - " + filmId + ", " + filmGenres);
        return filmGenres;
    }

    public void addGenreToFilm(Long filmId, Set<Long> genreIds) {
        String sql = "INSERT INTO FILM_GENRE (film_id, genre_id) VALUES (?, ?)";

        if (genreIds != null && !genreIds.isEmpty()) {
            log.info("Получен запрос на добавление жанра к фильму - " + filmId + ", жанр - " + genreIds);

            List<Object[]> batchArgs = genreIds.stream()
                    .map(genreId -> new Object[]{filmId, genreId})
                    .toList();

            jdbcTemplate.batchUpdate(sql, batchArgs);
        }
    }
}
