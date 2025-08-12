package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.dto.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage storage;

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Не задано имя пользователя, будет использован логин {}", user.getLogin());
        }
        log.info("Пользователь создан с логином {}", user.getLogin());
        return storage.addUser(user);
    }

    public Optional<User> updateUser(User user) {

        if (user.getId() == null) {
            log.info("Id пользователя должен быть указан");
            throw new NotFoundException("Id пользователя должен быть указан");
        }
        if (storage.getUserById(user.getId()).isPresent()) {
            log.info("Пользователь с id = {} обновлен", user.getId());
            return storage.updateUser(user);
        } else {
            throw new NotFoundException("Пользователь не найден с id = " + user.getId());
        }
    }

    public Optional<User> getUserById(Long id) {
        if (storage.getUserById(id).isPresent()) {
            return storage.getUserById(id);
        }
        throw new NotFoundException("Пользователь не найден с id = " + id);

    }

    public List<User> getAllUsers() {

        return storage.getAllUsers();
    }

    public void addToFriend(Long userId, Long friendId) {
        if (storage.getUserById(userId).isEmpty() || storage.getUserById(friendId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        storage.addFriends(userId, friendId);
        log.info("Пользователь {} стал другом пользователя {}", storage.getUserById(userId), storage.getUserById(friendId));
    }

    public void removeFromFriends(Long userId, Long friendId) {
        if (storage.getUserById(userId).isEmpty() || storage.getUserById(friendId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        storage.removeFriends(userId, friendId);
        log.info("Пользователи {} {} больше не друзья ", storage.getUserById(userId), storage.getUserById(friendId));
    }

    public List<User> getUsersFriends(Long userId) {
        if (storage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Вот список друзей пользователя {} ", storage.getUserById(userId));
        return storage.getFriends(userId);
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        if (storage.getUserById(userId).isEmpty() || storage.getUserById(friendId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return storage.getCommonFriends(userId, friendId);
    }
}