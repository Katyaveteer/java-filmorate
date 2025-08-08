package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.dto.User;

import java.util.List;
import java.util.Optional;


public interface UserStorage {

    User addUser(User user);

    Optional<User> updateUser(User user);

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    void deleteUser(Long id);

    void addFriends(Long userId, Long friendId);

    void removeFriends(Long userId, Long friendId);

    List<User> getFriends(Long id);

    List<User> getCommonFriends(Long id, Long friendId);

}
