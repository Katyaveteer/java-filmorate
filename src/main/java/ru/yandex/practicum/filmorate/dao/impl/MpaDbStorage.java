package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dao.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.dto.MpaRating;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate, MpaMapper mpaMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaMapper = mpaMapper;
    }

    @Override
    public List<MpaRating> getAllMpa() {
        String sql = "SELECT * FROM mpa_ratings ORDER BY mpa_id";
        return jdbcTemplate.query(sql, mpaMapper);
    }

    @Override
    public Optional<MpaRating> findMpaById(long id) {
        String sql = "SELECT * FROM mpa_ratings WHERE mpa_id = ?";
        return jdbcTemplate.query(sql, mpaMapper, id).stream().findFirst();
    }


}