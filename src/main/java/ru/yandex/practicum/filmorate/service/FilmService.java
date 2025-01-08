package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.dal.FilmLikesDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final FilmLikesDbStorage filmLikesDbStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage, FilmLikesDbStorage filmLikesDbStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmLikesDbStorage = filmLikesDbStorage;
    }

    public void addLike(Long filmId, Long userId) {
        if (!filmStorage.isFilmAlreadyCreatedById(filmId) || filmId == null) {
            log.error("Не существует фильма с id - " + filmId);
            throw new NotFoundException("Не существует фильма с id " + filmId);
        } else if (userStorage.getUserById(userId) == null) {
            log.error("Не существует пользователя с id - " + userId);
            throw new NotFoundException("Не существует пользователя с id - " + userId);
        } else {
            log.info("Пользователь {} лайкнул фильм {}", userId, filmId);
            filmLikesDbStorage.addLike(filmId, userId);
        }
    }

    public void deleteLike(Long filmId, Long userId) {
        if (!filmStorage.isFilmAlreadyCreatedById(filmId) || filmId == null) {
            log.error("Не существует фильма с id - " + filmId);
            throw new NotFoundException("Не существует фильма с id -" + filmId);
        } else if (userStorage.getUserById(userId) == null) {
            log.error("Не существует пользователя с id - " + userId);
            throw new NotFoundException("Не существует пользователя с id - " + userId);
        }
        filmLikesDbStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getMostLikedFilms(Long count) {
        if (filmStorage.getAllFilms() == null) {
            log.error("Список фильмов пуст");
            throw new NotFoundException("Список фильмов пуст");
        }

        log.info("Выдаётся список популярных фильмов");

        return filmStorage.getAllFilms().stream()
                .filter(film -> film.getLikedUsersIds() != null)
                .sorted(Comparator.comparingInt((Film film) -> film.getLikedUsersIds().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
