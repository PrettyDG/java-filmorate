package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    void createUser(User user);

    Collection<User> getAllUsers();

    User getUserById(Long userId);

    boolean isUserAlreadyCreatedById(Long userId);

    boolean isUserEmailAlreadyExist(String userEmail);

    void updateUser(Long userId, User user);

    long getNextId();
}
