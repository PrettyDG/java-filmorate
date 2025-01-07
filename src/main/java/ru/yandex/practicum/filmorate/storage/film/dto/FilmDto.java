package ru.yandex.practicum.filmorate.storage.film.dto;

import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class FilmDto {
    private Long filmId;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Long> likedUsersIDs = new HashSet<>();
    private List<Genre> genres;
    private Mpa mpa;
}
