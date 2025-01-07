package ru.yandex.practicum.filmorate.storage.film.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.mappers.GenreMapper;

import java.util.List;
import java.util.Optional;

@Repository("genreDbStorage")
@Slf4j
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM GENRE";
        log.info("Получен запрос получения всех жанров");
        return jdbcTemplate.query(sql, new GenreMapper());
    }

    public Optional<Genre> getGenreById(Long id) {
        String sql = "SELECT * FROM GENRE WHERE genre_id = ?";
        log.info("Получен запрос на получение жанра с id - " + id);
        return jdbcTemplate.query(sql, new GenreMapper(), id).stream().findFirst();
    }

}
