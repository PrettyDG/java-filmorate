package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
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
}
