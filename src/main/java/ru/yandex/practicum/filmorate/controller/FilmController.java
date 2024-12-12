package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }


    @GetMapping("/{filmId}")
    public Film getUserById(@PathVariable Long filmId) {
        log.info("Получен запрос на получение фильма с id - " + filmId);
        return inMemoryFilmStorage.getFilmById(filmId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        log.info("Получен запрос на получение популярных фильмов в кол-ве: " + count);
        return filmService.getMostLikedFilms(count);
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Получен запрос на получение всех фильмов");
        return inMemoryFilmStorage.getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос на создание фильма: " + film);

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка создания фильма: дата релиза раньше 28.12.1895");
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }

        inMemoryFilmStorage.createFilm(film);

        log.info("Фильм успешно создан с id: " + film.getId());
        return film;
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film likeFilm(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Получен запрос на лайк фильма: " + filmId + " от пользователя - " + userId);
        filmService.addLike(filmId, userId);
        return inMemoryFilmStorage.getFilmById(filmId);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Получен запрос на обновление фильма: " + newFilm);

        if (newFilm.getId() == null) {
            log.error("Ошибка обновления фильма: ID не указан");
            throw new ValidationException("Id должен быть указан");
        }
        if (inMemoryFilmStorage.isFilmAlreadyCreatedById(newFilm.getId())) {
            Film oldFilm = inMemoryFilmStorage.getFilmById(newFilm.getId());
            if (inMemoryFilmStorage.isFilmNameAlreadyExist(newFilm.getName())) {
                log.error("Ошибка обновления фильма: фильм с таким название уже существует");
                throw new ValidationException("Это название фильма уже используется");
            }

            oldFilm.setName(newFilm.getName());
            log.info("Название фильма обновлено - " + oldFilm.getName());

            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            log.info("Дата выхода фильма обновлена - " + oldFilm.getReleaseDate());

            if (newFilm.getDuration() != null) {
                oldFilm.setDuration(newFilm.getDuration());
                log.info("Длительность фильма обновлена - " + oldFilm.getDuration());
            }
            if (newFilm.getDescription() != null) {
                oldFilm.setDescription(newFilm.getDescription());
                log.info("Описание фильма обновлено - " + oldFilm.getDescription());
            }

            inMemoryFilmStorage.updateFilm(oldFilm);
            log.info("Фильм с id - " + oldFilm.getId() + " успешно обновлён");
            return oldFilm;
        }
        log.error("Ошибка обновления фильма: не найден фильм с id - " + newFilm.getId());
        throw new NotFoundException("Фильм с id = " + newFilm.getId() + " не найден");
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Получен запрос на удаление лайка к фильму: " + filmId + " от пользователя - " + userId);
        filmService.deleteLike(filmId, userId);
        return inMemoryFilmStorage.getFilmById(filmId);
    }
}
