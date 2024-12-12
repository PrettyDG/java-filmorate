package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        if (!userStorage.isUserAlreadyCreatedById(userId) || userId == null) {
            log.error("Не найден пользователь с id - " + userId);
            throw new NotFoundException("Не найден пользователь с id - " + userId);
        } else if (!userStorage.isUserAlreadyCreatedById(friendId) || friendId == null) {
            log.error("Не найден пользователь с id - " + friendId);
            throw new NotFoundException("Не найден пользователь с id - " + friendId);
        }
        if (userStorage.getUserById(userId).getFriends() == null) {
            Set<Long> friends = new HashSet<>();
            friends.add(friendId);
            userStorage.getUserById(userId).setFriends(friends);
        } else {
            userStorage.getUserById(userId).getFriends().add(friendId);
        }

        if (userStorage.getUserById(friendId).getFriends() == null) {
            Set<Long> friends = new HashSet<>();
            friends.add(userId);
            userStorage.getUserById(friendId).setFriends(friends);
        } else {
            userStorage.getUserById(friendId).getFriends().add(userId);
        }

        log.info("Пользователи {} и {} теперь друзья", userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (!userStorage.isUserAlreadyCreatedById(userId)) {
            log.error("Не существует пользователя с id - " + userId);
            throw new NotFoundException("Не найден пользователь с id - " + userId);
        } else if (!userStorage.isUserAlreadyCreatedById(friendId)) {
            log.error("Не существует пользователя с id - " + userId);
            throw new NotFoundException("Не найден пользователь с id - " + friendId);
        } else if (userStorage.getUserById(userId).getFriends() == null
                || userStorage.getUserById(friendId).getFriends() == null) {
            log.info("У пользователя нет друзей");
            return;
        }

        userStorage.getUserById(userId).getFriends().remove(friendId);

        userStorage.getUserById(friendId).getFriends().remove(userId);

        log.info("Пользователи {} и {} теперь не в друзьях", userId, friendId);
    }

    private Set<Long> getAllFriendsIds(Long userId) {
        if (userStorage.getUserById(userId) == null) {
            log.error("Не существует пользователя с id - " + userId);
            throw new NotFoundException("Не существует пользователя с id - " + userId);
        }
        log.info("Выдаётся список всех id друзей для пользователя - " + userId);
        return userStorage.getUserById(userId).getFriends();
    }

    public Collection<User> getAllFriends(Long userId) {
        if (getAllFriendsIds(userId) == null) {
            log.info("У пользователя с id - {} нет друзей", userId);
            return Collections.emptySet();
        }
        log.info("Выдаётся список всех друзей для пользователя - " + userId);
        return getAllFriendsIds(userId).stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public Collection<User> getMutualFriends(Long firstUserId, Long secondUserId) {
        if (getAllFriends(firstUserId) == null) {
            log.error("Нету друзей у пользователя с id - " + firstUserId);
            throw new NotFoundException("Нету друзей у пользователя с id - " + firstUserId);
        }
        log.info("Выдаётся список друзей для пользователей {} и {}", firstUserId, secondUserId);
        return getAllFriendsIds(firstUserId).stream()
                .filter(getAllFriendsIds(secondUserId)::contains)
                .map(userStorage::getUserById)
                .toList();
    }
}
