package ru.yandex.practicum.filmorate.storage.film.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.mappers.FilmMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;
    private final Map<Long, Film> films = new HashMap<>();

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmMapper filmMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMapper = filmMapper;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT * FROM FILM";
        log.info("Получен запрос на получение всех фильмов");
        return jdbcTemplate.query(sql, filmMapper);
    }

    @Override
    public void createFilm(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);

        String sql = "INSERT INTO FILM (film_id, name, description, releaseDate, duration, mpaID) VALUES (?, ?, ?, ?, ?, ?)";
        log.info("Получен запрос на создание фильма - " + film);
        jdbcTemplate.update(sql, film.getId(), film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
    }

    @Override
    public Film getFilmById(Long filmId) {
        String sql = "SELECT * FROM FILM WHERE film_id = ?";
        log.info("Получен запрос на получения фильма с id - " + filmId);
        return jdbcTemplate.queryForObject(sql, filmMapper, filmId);
    }

    @Override
    public boolean isFilmAlreadyCreatedById(Long filmId) {
        String sql = "SELECT COUNT(*) FROM FILM WHERE film_id = ?";
        log.info("Получен запрос узнать, создан ли уже фильм с id - " + filmId);
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, filmId);
        return count != null && count > 0;
    }

    @Override
    public boolean isFilmNameAlreadyExist(String filmName) {
        String sql = "SELECT COUNT(*) FROM FILM WHERE name = ?";
        log.info("Получен запрос узнать, создан ли уже фильм с названием - " + filmName);
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, filmName);
        return count != null && count > 0;
    }

    @Override
    public void updateFilm(Film film) {
        String sql = "UPDATE FILM SET name = ?, description = ?, releaseDate = ?, duration = ? WHERE film_id = ?";
        log.info("Получен запрос на обновление фильма - " + film);
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getId());
    }

    @Override
    public long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
