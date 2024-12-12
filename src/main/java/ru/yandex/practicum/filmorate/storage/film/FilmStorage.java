package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    void createFilm(Film film);

    Collection<Film> getAllFilms();

    Film getFilmById(Long filmId);

    boolean isFilmAlreadyCreatedById(Long filmId);

    boolean isFilmNameAlreadyExist(String filmName);

    void updateFilm(Film film);

    long getNextId();
}
