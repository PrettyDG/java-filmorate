package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получен запрос на получение всех пользователей");
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя: " + user);

        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        users.put(user.getId(), user);

        log.info("Пользователь успешно создан с id: " + user.getId());
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Получен запрос на обновление пользователя: " + newUser);

        if (newUser.getId() == null) {
            log.error("Ошибка обновления пользователя: ID не указан");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            if (users.values().stream().map(User::getEmail).anyMatch(email -> email.equals(newUser.getEmail()))) {
                log.error("Ошибка обновления пользователя: email уже используется");
                throw new ValidationException("Этот имейл уже используется");
            }

            if (newUser.getEmail() != null) {
                oldUser.setEmail(newUser.getEmail());
                log.info("Email пользователя успешно обновлён - " + oldUser.getEmail());
            }
            if (newUser.getLogin() != null) {
                oldUser.setLogin(newUser.getLogin());
                log.info("Логин пользователя успешно обновлён - " + oldUser.getLogin());
            }
            if (newUser.getName() != null) {
                oldUser.setName(newUser.getName());
                log.info("Имя пользователя успешно обновлено - " + oldUser.getName());
            }
            if (newUser.getBirthday() != null) {
                oldUser.setBirthday(newUser.getBirthday());
                log.info("Дата рождения пользователя успешно обновлена - " + oldUser.getBirthday());
            }

            log.info("Пользователь с id - " + oldUser.getId() + " успешно обновлён");
            return oldUser;
        }
        log.error("Ошибка обновления пользователя: не найден пользователь с id - " + newUser.getId());
        throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
