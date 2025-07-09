package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {

        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User getById(Long id) {
        User user = userStorage.getById(id);
        if (user == null) {
            String error = "Пользователь с id " + id + " не найден";
            log.error(error);
            throw new NotFoundException(error);
        }
        return user;
    }

    public void addFriend(Long userId, Long friendId) {
        if (userId.equals(friendId)) {
            throw new ValidationException("Нельзя добавить самого себя в друзья.");
        }

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.info("Пользователь {} добавил в друзья пользователя {}", userId, friendId);
    }

    public void removeFriend(Long userId, Long friendId) {

        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.info("Пользователь {} удалил из друзейпользователя {}", userId, friendId);

    }

    public Collection<User> getFriends(Long userId) {
        return userStorage.getById(userId).getFriends().stream()
                .map(userStorage::getById)
                .toList();


    }


    public List<User> getCommonFriends(Long userId, Long otherId) {
        Set<Long> common = new HashSet<>(getById(userId).getFriends());
        common.retainAll(getById(otherId).getFriends());

        return common.stream()
                .map(this::getById)
                .toList();

    }
}



