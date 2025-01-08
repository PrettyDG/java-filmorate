package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.exceptions.ValidationConstants;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;


@Data
@AllArgsConstructor
@Builder
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @Min(1)
    private Long duration;
    private Set<Long> likedUsersIds;
    private LinkedHashSet<Genre> genres;
    private Mpa mpa;

    @AssertTrue
    public boolean isMpaIdValid() {
        return mpa == null || mpa.getId() <= ValidationConstants.MAX_MPA_ID;
    }

    @AssertTrue
    public boolean isGenreIdValid() {
        if (genres != null) {
            if (genres.stream().allMatch(g -> g.getId() >= ValidationConstants.MAX_VALID_GENRE_ID) && !genres.isEmpty()) {
                throw new ValidationException("Жанр неправильный");
            }
        }
        return true;
    }
}