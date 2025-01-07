package ru.yandex.practicum.filmorate.storage.film.dto;

import lombok.Data;

@Data
public class FilmGenresDto {
    private Long filmId;
    private Long genreId;
}
