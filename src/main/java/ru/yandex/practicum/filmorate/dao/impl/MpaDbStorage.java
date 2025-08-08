package ru.yandex.practicum.filmorate.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dto.MpaRating;

import java.util.List;


@AllArgsConstructor
@Component
@Primary
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MpaRating> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM rating_mpa", new DataClassRowMapper<>(MpaRating.class));
    }

    @Override
    public MpaRating getMpaById(long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM rating_mpa WHERE id = ?", new DataClassRowMapper<>(MpaRating.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public MpaRating getMpaOfFilm(Long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM rating_mpa WHERE id IN (SELECT rating_mpa_id FROM films WHERE id = ?);", new DataClassRowMapper<>(MpaRating.class), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

