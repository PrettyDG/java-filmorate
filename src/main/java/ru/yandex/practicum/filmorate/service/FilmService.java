package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long filmId, Long userId) {
        if (!filmStorage.isFilmAlreadyCreatedById(filmId) || filmId == null) {
            log.error("Не существует фильма с id - " + filmId);
            throw new NotFoundException("Не существует фильма с id " + filmId);
        } else if (userStorage.getUserById(userId) == null) {
            log.error("Не существует пользователя с id - " + userId);
            throw new NotFoundException("Не существует пользователя с id - " + userId);
        } else if (filmStorage.getFilmById(filmId).getLikedUsersIds() == null) {
            Set<Long> filmLikes = new HashSet<>();
            filmLikes.add(userId);
            filmStorage.getFilmById(filmId).setLikedUsersIds(filmLikes);
        } else {
            log.info("Пользователь {} лайкнул фильм {}", userId, filmId);
            filmStorage.getFilmById(filmId).getLikedUsersIds().add(userId);
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
        filmStorage.getFilmById(filmId).getLikedUsersIds().remove(userId);
    }

    public Collection<Film> getMostLikedFilms(Long count) {
        if (filmStorage.getAllFilms() == null) {
            log.error("Список фильмов пуст");
            throw new NotFoundException("Список фильмов пуст");
        }

        log.info("Выдаётся список популярных фильмов");

        return filmStorage.getAllFilms().stream()
                .filter(film -> film.getLikedUsersIds() != null)
                .sorted(Comparator.comparingInt(film -> film.getLikedUsersIds().size()))
                .limit(count)
                .collect(Collectors.toSet());
    }
}
