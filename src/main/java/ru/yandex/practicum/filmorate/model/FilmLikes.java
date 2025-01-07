package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilmLikes {
    private Long filmId;
    private Long userId;
}
