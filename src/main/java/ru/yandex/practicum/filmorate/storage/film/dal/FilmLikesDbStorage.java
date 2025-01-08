package ru.yandex.practicum.filmorate.storage.film.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmLikes;
import ru.yandex.practicum.filmorate.storage.film.mappers.FilmLikesMapper;

import java.util.List;

@Repository("filmLikesDbStorage")
@Slf4j
public class FilmLikesDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmLikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<FilmLikes> findAll() {
        String sql = "SELECT * FROM filmLikedByUsers";
        log.info("Получен запрос на получениях всех фильмов");
        return jdbcTemplate.query(sql, new FilmLikesMapper());
    }

    public List<FilmLikes> findLikesByFilmId(Long id) {
        String sql = "SELECT * FROM filmLikedByUsers WHERE film_id = ?";
        log.info("Получен запрос узнать все лайки у фильма с id - " + id);
        return jdbcTemplate.query(sql, new FilmLikesMapper(), id);
    }

    public FilmLikes addLike(Long filmId, Long userId) {
        FilmLikes filmLikes = new FilmLikes();
        filmLikes.setFilmId(filmId);
        filmLikes.setUserId(userId);

        String sql = "INSERT INTO filmLikedByUsers (film_id, userID) VALUES (?, ?)";
        log.info("Получен запрос на лайк фильма с id - " + filmId + ", от пользователя - " + userId);

        jdbcTemplate.update(sql, filmId, userId);
        return filmLikes;
    }

    public void deleteLike(Long filmId, Long userId) {
        String sql = "DELETE FROM filmLikedByUsers WHERE film_id = ? AND userID = ?";
        log.info("Получен запрос на удаление лайка фильма с id - " + filmId + ", от пользователя - " + userId);
        jdbcTemplate.update(sql, filmId, userId);
    }
}
