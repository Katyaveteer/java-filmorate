package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.dao.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.dto.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreMapper genreMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreMapper = genreMapper;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres ORDER BY genre_id";
        return jdbcTemplate.query(sql, genreMapper);
    }

    @Override
    public Optional<Genre> findGenreById(long id) {
        String sql = "SELECT * FROM genres WHERE genre_id = ?";
        return jdbcTemplate.query(sql, genreMapper, id).stream().findFirst();
    }

    @Override
    public List<Genre> getGenresByFilmId(long filmId) {
        String sql = "SELECT g.* FROM genres g JOIN film_genres fg ON g.id = fg.genre_id WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, genreMapper, filmId);
    }


}