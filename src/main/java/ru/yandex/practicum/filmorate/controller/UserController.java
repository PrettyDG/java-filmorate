package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        log.info("Получен запрос на получение пользователя с id - " + userId);
        return userStorage.getUserById(userId);
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получен запрос на получение всех пользователей");
        return userStorage.getAllUsers();
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getUserFriends(@PathVariable Long userId) {
        log.info("Получен запрос на получение всех друзей пользователя с id - " + userId);
        return userService.getAllFriends(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public Collection<User> getMutualFriends(@PathVariable Long userId, @PathVariable Long otherId) {
        log.info("Получен запрос на получение общий друзей пользователей с id - " + userId + ", " + otherId);
        return userService.getMutualFriends(userId, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Получен запрос на создание пользователя: " + user);

        userStorage.createUser(user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Получен запрос на обновление пользователя: " + newUser);

        if (newUser.getId() == null) {
            log.error("Ошибка обновления пользователя: ID не указан");
            throw new ValidationException("Id должен быть указан");
        }
        if (userStorage.isUserAlreadyCreatedById(newUser.getId())) {
            User oldUser = userStorage.getUserById(newUser.getId());

            if (userStorage.isUserEmailAlreadyExist(newUser.getEmail())) {
                log.error("Ошибка обновления пользователя: email уже используется");
                throw new ValidationException("Этот имейл уже используется");
            }

            oldUser.setEmail(newUser.getEmail());
            log.info("Email пользователя успешно обновлён - " + oldUser.getEmail());

            oldUser.setLogin(newUser.getLogin());
            log.info("Логин пользователя успешно обновлён - " + oldUser.getLogin());

            oldUser.setBirthday(newUser.getBirthday());
            log.info("Дата рождения пользователя успешно обновлена - " + oldUser.getBirthday());

            if (newUser.getName() != null) {
                oldUser.setName(newUser.getName());
                log.info("Имя пользователя успешно обновлено - " + oldUser.getName());
            }

            userStorage.updateUser(oldUser.getId(), oldUser);
            log.info("Пользователь с id - " + oldUser.getId() + " успешно обновлён");

            return oldUser;
        }
        log.error("Ошибка обновления пользователя: не найден пользователь с id - " + newUser.getId());
        throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public User addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос на добавление в друзья от: " + userId + ", к: " + friendId);
        userService.addFriend(userId, friendId);
        return userStorage.getUserById(friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public User deleteFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос на удаление из друзей от: " + userId + ", к: " + friendId);
        userService.deleteFriend(userId, friendId);
        return userStorage.getUserById(friendId);
    }
}
