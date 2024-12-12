package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public void createUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }

        user.setId(getNextId());
        log.info("Пользователь успешно создан с id: " + user);
        users.put(user.getId(), user);
    }

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User getUserById(Long userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Не существует пользователя с id - " + userId);
        }
        return users.get(userId);
    }

    @Override
    public boolean isUserAlreadyCreatedById(Long userId) {
        return users.containsKey(userId);
    }

    @Override
    public boolean isUserEmailAlreadyExist(String userEmail) {
        return users.values().stream().map(User::getEmail).anyMatch(email -> email.equals(userEmail));
    }

    @Override
    public void updateUser(Long userId, User user) {
        users.put(userId, user);
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
