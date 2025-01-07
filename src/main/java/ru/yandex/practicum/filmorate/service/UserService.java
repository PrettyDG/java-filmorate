package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friends;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.storage.user.dal.FriendsDbStorage;
import ru.yandex.practicum.filmorate.storage.user.dal.FriendshipStatusDbStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendsDbStorage friendsDbStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendsDbStorage friendsDbStorage, FriendshipStatusDbStorage friendshipStatusDbStorage) {
        this.userStorage = userStorage;
        this.friendsDbStorage = friendsDbStorage;
    }

    public void addFriend(Long userId, Long friendId) {
        if (!userStorage.isUserAlreadyCreatedById(userId) || userId == null) {
            log.error("Не найден пользователь с id - " + userId);
            throw new NotFoundException("Не найден пользователь с id - " + userId);
        } else if (!userStorage.isUserAlreadyCreatedById(friendId) || friendId == null) {
            log.error("Не найден пользователь с id - " + friendId);
            throw new NotFoundException("Не найден пользователь с id - " + friendId);
        }

        if (!friendsDbStorage.allFriendsToUser(userId).isEmpty()) {
            if (friendsDbStorage.allFriendsToUser(userId).contains(friendId)) {
                Friends friend = friendsDbStorage.allFriendsToUser(userId).stream()
                        .filter(f -> f.getFriendId().equals(friendId))
                        .findFirst()
                        .orElse(null);
                if (friend.getStatusId() == 1) {
                    friendsDbStorage.acceptFriendStatus(userId, friendId);
                    log.info("ОДОБРЕН запрос в друзья от id {} к id {}", userId, friendId);
                }
            }
        } else if (!friendsDbStorage.allFriendsToUser(friendId).isEmpty()) {
            if (friendsDbStorage.allFriendsToUser(friendId).contains(userId)) {
                Friends friend = friendsDbStorage.allFriendsToUser(friendId).stream()
                        .filter(f -> f.getFriendId().equals(userId))
                        .findFirst()
                        .orElse(null);
                if (friend.getStatusId() == 1) {
                    friendsDbStorage.acceptFriendStatus(userId, friendId);
                    log.info("ОДОБРЕН запрос в друзья от id {} к id {}", userId, friendId);
                }
            }
        } else {
            log.info("ПОЛУЧЕН запрос в друзья от id {} к id {}", userId, friendId);
            friendsDbStorage.addNewFriends(userId, friendId);
        }
    }

    public void deleteFriend(Long userId, Long friendId) {
        if (!userStorage.isUserAlreadyCreatedById(userId)) {
            log.error("Не существует пользователя с id - " + userId);
            throw new NotFoundException("Не найден пользователь с id - " + userId);
        } else if (!userStorage.isUserAlreadyCreatedById(friendId)) {
            log.error("Не существует пользователя с id - " + userId);
            throw new NotFoundException("Не найден пользователь с id - " + friendId);
        } else if (friendsDbStorage.allFriendsToUser(userId) == null &&
                friendsDbStorage.allFriendsToUser(friendId) == null) {
            log.info("У пользователя нет друзей");
            return;
        }


        if (friendsDbStorage.getFriendById(userId, friendId) != null && friendsDbStorage.getFriendById(userId, friendId).getStatusId() == 2) {
            friendsDbStorage.updateFriend(friendId, userId);
        }

        friendsDbStorage.deleteFriend(userId, friendId);

        log.info("Пользователи {} и {} теперь не в друзьях", userId, friendId);
    }

    private Set<Long> getAllFriendsIds(Long userId) {
        if (userStorage.getUserById(userId) == null) {
            log.error("Не существует пользователя с id - " + userId);
            throw new NotFoundException("Не существует пользователя с id - " + userId);
        } else if (friendsDbStorage.allFriendsToUser(userId) == null) {
            log.error("У пользователя пока нет друзей");
            throw new NotFoundException("Не существует друзей у пользователя с id " + userId);
        }

        log.info("Выдаётся список всех id друзей для пользователя - " + userId);
        return friendsDbStorage.allFriendsToUser(userId).stream().map(Friends::getFriendId).collect(Collectors.toSet());
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
