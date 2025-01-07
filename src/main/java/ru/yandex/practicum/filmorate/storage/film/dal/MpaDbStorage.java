package ru.yandex.practicum.filmorate.storage.film.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.mappers.MpaMapper;

import java.util.List;
import java.util.Optional;

@Repository("mpaDbStorage")
@Slf4j
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getAll() {
        String sql = "SELECT * FROM MPA";
        log.info("Получен запрос на получения всех MPA");
        return jdbcTemplate.query(sql, new MpaMapper());
    }

    public Optional<Mpa> getMpaById(Long id) {
        String sql = "SELECT * FROM MPA WHERE mpaID = ?";
        log.info("Получен запрос на получения MPA с id - " + id);
        return jdbcTemplate.query(sql, new MpaMapper(), id).stream().findFirst();
    }
}
