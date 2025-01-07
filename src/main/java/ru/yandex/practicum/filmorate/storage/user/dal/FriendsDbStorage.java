package ru.yandex.practicum.filmorate.storage.user.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.storage.user.mappers.FriendsMapper;

import java.util.List;

@Repository("friendsDbStorage")
@Slf4j
public class FriendsDbStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void deleteFriend(Long id1, Long id2) {
        String sql = "DELETE FROM friends where userID = ? AND friendID = ?";
        log.info("Удаление пользователя из друзей - " + id1 + ", " + id2);
        jdbcTemplate.update(sql, id1, id2);
    }

    public void updateFriend(Long id1, Long id2) {
        String sql = "UPDATE friends SET friendshipStatusID = 1 WHERE userID = ? AND friendID = ?";
        log.info("Пользователь - " + id2 + " удалил из друзей - " + id1);
        jdbcTemplate.update(sql, id1, id2);
    }

    public List<Friends> allFriendsToUser(Long id1) {
        String sql = "SELECT * FROM friends WHERE userID = ?";
        log.info("Получение всех друзей пользователя - " + id1);
        return jdbcTemplate.query(sql, new FriendsMapper(), id1);
    }

    public Friends getFriendById(Long id1, Long id2) {
        String sql = "SELECT * FROM friends WHERE userID = ? AND friendID = ?";
        log.info("Узнать, в друзьях ли пользователи - " + id1 + ", " + id2);

        return jdbcTemplate.query(sql, new FriendsMapper(), id1, id2).stream().findFirst().orElse(null);
    }

    public void acceptFriendStatus(Long id1, Long id2) {
        String sql = "UPDATE friends SET friendshipStatusID = 2 WHERE userID = ? AND friendID = ?";
        log.info("Принятие дружбы у пользователей - " + id1 + ", " + id2);
        jdbcTemplate.update(sql, id1, id2);
        jdbcTemplate.update(sql, id2, id1);
    }

    public void addNewFriends(Long id1, Long id2) {
        String sql = "INSERT INTO friends (userID, friendID, friendshipStatusID) VALUES (?, ?, ?)";
        log.info("Запрос в друзья от - " + id1 + " к " + id2);
        jdbcTemplate.update(sql, id1, id2, 1);
    }
}