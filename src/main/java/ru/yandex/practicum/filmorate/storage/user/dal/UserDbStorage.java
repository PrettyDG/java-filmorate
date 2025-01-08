package ru.yandex.practicum.filmorate.storage.user.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.mappers.UserMapper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Map<Long, User> users = new HashMap<>();

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createUser(User user) {
        user.setId(getNextId());
        users.put(user.getId(), user);

        String sql = "INSERT INTO USERS (id, email, login, name, birthday) VALUES (?, ?, ?, ?, ?)";
        log.info("Создан пользователь - " + user);
        jdbcTemplate.update(sql, user.getId(), user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "SELECT * FROM USERS";
        log.info("Получение всех пользователей - ");
        return jdbcTemplate.query(sql, new UserMapper());
    }

    @Override
    public User getUserById(Long userId) {
        log.info("Получен запрос на получение пользователя по ID - " + userId);
        String sql = "SELECT * FROM USERS WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new UserMapper(), userId);
        } catch (EmptyResultDataAccessException e) {
            log.error("Произошла ошибка по время получения пользователя по ID - " + e.getMessage());
            return null;
        }
    }

    @Override
    public boolean isUserAlreadyCreatedById(Long userId) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE id = ?";
        log.info("Получен запрос узнать, создан ли уже пользователь с id - " + userId);
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null && count > 0;
    }

    @Override
    public boolean isUserEmailAlreadyExist(String userEmail) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE email = ?";
        log.info("Получен запрос узнать, создан ли уже пользователь с email - " + userEmail);
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userEmail);
        return count != null && count > 0;
    }

    @Override
    public void updateUser(Long userId, User user) {
        String sql = "UPDATE USERS SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        log.info("Получен запрос на обновление пользователя с id - " + userId + " - " + user);
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), userId);
    }

    @Override
    public long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
