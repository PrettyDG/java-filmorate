package ru.yandex.practicum.filmorate.storage.film.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.film.dal.FilmGenresDbStorage;
import ru.yandex.practicum.filmorate.storage.film.dal.FilmLikesDbStorage;
import ru.yandex.practicum.filmorate.storage.film.dal.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.film.dal.MpaDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class FilmMapper implements RowMapper<Film> {
    private final MpaDbStorage mpaDbStorage;
    private final GenreDbStorage genreDbStorage;
    private final FilmGenresDbStorage filmGenresDbStorage;
    private final FilmLikesDbStorage filmLikesDbStorage;

    @Autowired
    public FilmMapper(MpaDbStorage mpaDbStorage, GenreDbStorage genreDbStorage, FilmGenresDbStorage filmGenresDbStorage, FilmLikesDbStorage filmLikesDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
        this.genreDbStorage = genreDbStorage;
        this.filmGenresDbStorage = filmGenresDbStorage;
        this.filmLikesDbStorage = filmLikesDbStorage;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getLong("film_id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("releaseDate").toLocalDate())
                .duration(rs.getLong("duration"))
                .build();


        long mpaId = rs.getLong("mpaID");
        if (!rs.wasNull()) {
            Optional<Mpa> mpaOpt = mpaDbStorage.getMpaById(mpaId);
            if (mpaOpt.isPresent()) {
                Mpa mpa = new Mpa();
                mpa.setId(mpaId);
                mpa.setName(mpaOpt.get().getName());
                film.setMpa(mpa);
            }
        }


        if (filmGenresDbStorage.findAllGenresByFilmId(film.getId()) != null) {
            List<FilmGenre> genreIdsForFilm = filmGenresDbStorage.findAllGenresByFilmId(film.getId());
            LinkedHashSet<Genre> genresForFilm = new LinkedHashSet<>();
            List<Genre> allGenres = genreDbStorage.getAllGenres();
            Map<Long, String> genreMap = allGenres.stream()
                    .collect(Collectors.toMap(Genre::getId, Genre::getName));

            for (FilmGenre filmGenre : genreIdsForFilm) {
                Genre currentGenre = new Genre();
                Long genreId = filmGenre.getGenreId();

                currentGenre.setId(genreId);
                currentGenre.setName(genreMap.get(genreId));

                genresForFilm.add(currentGenre);
            }

            film.setGenres(genresForFilm);
        }


        if (filmLikesDbStorage.findLikesByFilmId(film.getId()) != null) {
            List<FilmLikes> likes = filmLikesDbStorage.findLikesByFilmId(film.getId());
            Set<Long> userIds = new HashSet<>();
            for (FilmLikes like : likes) {
                userIds.add(like.getUserId());
            }
            film.setLikedUsersIds(userIds);
        }


        return film;
    }
}
