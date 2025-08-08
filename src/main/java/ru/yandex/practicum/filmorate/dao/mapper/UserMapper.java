package ru.yandex.practicum.filmorate.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class UserMapper implements RowMapper<User> {
    Map<Long, User> userMap = new HashMap<>();

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long userId = rs.getLong("id");
        User user = userMap.get(userId);

        if (user == null) {
            user = new User();
            user.setId(userId);
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            user.setFriends(new HashSet<>());

            userMap.put(userId, user);
        }
        if (rs.getLong("friend_id") != 0) {
            user.getFriends().add(rs.getLong("friend_id"));
        }
        if (rs.isLast()) {
            userMap.clear();
        }

        return user;

    }
}
