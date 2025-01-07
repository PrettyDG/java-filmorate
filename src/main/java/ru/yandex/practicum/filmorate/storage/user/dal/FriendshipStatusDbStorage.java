package ru.yandex.practicum.filmorate.storage.user.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.storage.user.mappers.FriendshipMapper;

import java.util.List;

@Repository("friendshipStatusDbStorage")
@Slf4j
public class FriendshipStatusDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendshipStatusDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public FriendshipStatus getStatus(Long statusId) {
        String sql = "SELECT * FROM friendshipStatus WHERE friendshipStatusID = ?";
        log.info("Получен запрос узнать статус дружбы с id - " + statusId);
        return jdbcTemplate.query(sql, new FriendshipMapper(), statusId).getFirst();
    }

    public List<FriendshipStatus> getAll() {
        String sql = "SELECT * FROM friendshipStatus";
        log.info("Получен запрос узнать все статусы дружбы");
        return jdbcTemplate.query(sql, new FriendshipMapper());
    }
}
