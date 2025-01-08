package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.dal.GenreDbStorage;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final GenreDbStorage genreDbStorage;

    @Autowired
    public GenreController(GenreDbStorage genreDbStorage) {
        this.genreDbStorage = genreDbStorage;
    }

    @GetMapping
    public List<Genre> getAll() {
        log.info("Получен запрос на получение всех жанров");
        if (genreDbStorage.getAllGenres().isEmpty()) {
            log.error("Список жанров пуст");
            throw new NotFoundException("Список жанров пуст");
        }
        return genreDbStorage.getAllGenres();
    }

    @GetMapping("/{genreId}")
    public Genre getById(@PathVariable Long genreId) {
        log.info("Получен запрос на получение одного жанра");
        if (genreDbStorage.getGenreById(genreId).isEmpty()) {
            log.error("Не существует жанра с таким ID - " + genreId);
            throw new NotFoundException("Не существует жанра с таким ID");
        }
        return genreDbStorage.getGenreById(genreId).get();
    }
}
