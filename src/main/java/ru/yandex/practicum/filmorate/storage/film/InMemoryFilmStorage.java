package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public void createFilm(Film film) {
        film.setId(getNextId());
        log.info("Создан фильм - " + film);
        films.put(film.getId(), film);
    }

    @Override
    public Collection<Film> getAllFilms() {
        log.info("Выдаётся список всех фильмов");
        return films.values();
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (films.get(filmId) == null) {
            log.error("Фильм с id {} не найден", filmId);
            throw new NotFoundException("Не найден фильм с id - " + filmId);
        }
        return films.get(filmId);
    }

    @Override
    public boolean isFilmAlreadyCreatedById(Long filmId) {
        return films.containsKey(filmId);
    }

    @Override
    public boolean isFilmNameAlreadyExist(String filmName) {
        return films.values().stream().map(Film::getName).anyMatch(name -> name.equals(filmName));
    }

    @Override
    public void updateFilm(Film film) {
        log.info("Обновлён фильм - " + film);
        films.put(film.getId(), film);
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
