package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dal.MpaDbStorage;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaController {
    private final MpaDbStorage mpaDbStorage;

    @Autowired
    public MpaController(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    @GetMapping
    public List<Mpa> getAll() {
        log.info("Получен запрос на получение всех MPA");
        if (mpaDbStorage.getAll().isEmpty()) {
            log.error("Список MPA пуст");
            throw new NotFoundException("Список MPA пуст");
        }
        return mpaDbStorage.getAll();
    }

    @GetMapping("/{mpaId}")
    public Mpa getMpaById(@PathVariable Long mpaId) {
        log.info("Получен запрос на получение MPA по id");
        if (mpaDbStorage.getMpaById(mpaId).isEmpty()) {
            log.error("MPA с таким ID не существует - " + mpaId);
            throw new NotFoundException("MPA с таким ID не существует");
        }
        return mpaDbStorage.getMpaById(mpaId).get();
    }
}
