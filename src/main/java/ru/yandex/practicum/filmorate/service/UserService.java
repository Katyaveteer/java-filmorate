package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;


@Getter
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {

        this.userStorage = userStorage;
    }

    public User create(User user) {
        return userStorage.add(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User getById(Long id) {
        return userStorage.getById(id);
    }

    public void delete(Long id) {
        userStorage.delete(id);
    }


    public void addFriend(Long userId, Long friendId) {
        try {
            userStorage.addFriend(userId, friendId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        try {
            userStorage.removeFriend(userId, friendId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Пользователь не найден");
        }

    }

    public List<User> getFriends(Long userId) {
        try {
            return userStorage.getFriends(userId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Пользователь не найден");
        }

    }

    public List<User> getCommonFriends(Long userId, Long otherId) {
        try {
            return userStorage.getCommonFriends(userId, otherId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Пользователь не найден");
        }

    }
}
