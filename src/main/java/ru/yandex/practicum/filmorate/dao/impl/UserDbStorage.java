package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.dao.mapper.UserMapper;
import ru.yandex.practicum.filmorate.dto.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;


@AllArgsConstructor
@Repository
@Qualifier("userDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    @Override
    public User addUser(User user) {
        String sqlQuery =
                "INSERT INTO users (email, login, name, birthday)values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public Optional<User> updateUser(User user) {
        String sqlQuery =
                "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(
                sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return Optional.of(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        List<User> users = jdbcTemplate.query("SELECT " +
                "u.ID, " +
                "u.EMAIL, " +
                "u.LOGIN, " +
                "u.NAME, " +
                "u.BIRTHDAY, " +
                "f.friend_id " +
                "FROM USERS AS u " +
                "LEFT JOIN FRIENDS AS f ON (f.user_id  = u.ID)" +
                "WHERE u.id = ?", userMapper, id);
        if (!users.isEmpty()) {
            return Optional.ofNullable(users.getFirst());
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = jdbcTemplate.query("SELECT " +
                "u.ID, " +
                "u.EMAIL, " +
                "u.LOGIN, " +
                "u.NAME, " +
                "u.BIRTHDAY, " +
                "f.friend_id " +
                "FROM USERS u " +
                "LEFT JOIN FRIENDS f ON (f.user_id  = u.ID)", userMapper);
        Set<User> uniqueUser = new TreeSet<>(Comparator.comparing(User::getId));
        uniqueUser.addAll(users);
        return new ArrayList<>(uniqueUser);
    }

    @Override
    public void deleteUser(Long id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ? ", id);
    }

    @Override
    public void addFriends(Long userId, Long friendId) {
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id, status)values (?, ?, ?)", userId, friendId, true);
    }

    @Override
    public void removeFriends(Long userId, Long friendId) {
        jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? AND friend_id = ?", userId, friendId);
    }

    @Override
    public List<User> getFriends(Long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id IN (SELECT friend_id FROM friends WHERE user_id = ? AND status = true)", new DataClassRowMapper<>(User.class), id);
    }

    @Override
    public List<User> getCommonFriends(Long id, Long friendId) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id IN (SELECT friend_id FROM friends WHERE user_id = ? AND status = true AND friend_id IN ( SELECT friend_id FROM friends WHERE user_id = ? AND status = true))", new DataClassRowMapper<>(User.class), id, friendId);
    }
}