package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dao.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.dto.Genre;
import ru.yandex.practicum.filmorate.dto.MpaRating;
import ru.yandex.practicum.filmorate.dto.User;
import ru.yandex.practicum.filmorate.exception.AlreadyExistsException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;


@Repository
@Qualifier("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final FilmMapper filmMapper;


    public Film addFilm(Film film) {

        final String sql = "INSERT INTO films (name, release_date, description, duration, mpa_id) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, film.getTitle());
            preparedStatement.setDate(2, Date.valueOf(film.getReleaseDate())); // Используем Date.valueOf()
            preparedStatement.setString(3, film.getDescription());
            preparedStatement.setInt(4, film.getDuration());
            preparedStatement.setInt(5, film.getMpa().getId());
            return preparedStatement;
        }, generatedKeyHolder);

        // 2. Устанавливаем сгенерированный ID фильма
        long filmId = Objects.requireNonNull(generatedKeyHolder.getKey()).intValue();
        film.setId(filmId);

        if (film.getMpa() != null) {
            MpaRating mpa = mpaStorage.getMpaById(film.getMpa().getId());
            if (mpa == null) {
                throw new IllegalArgumentException("MPA с ID " + film.getMpa().getId() + " не найден");
            }
            film.setMpa(mpa);
        }

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            Set<Genre> updatedGenres = new HashSet<>();
            for (Genre genre : film.getGenres()) {
                Genre fullGenre = genreStorage.getById(genre.getId());
                if (fullGenre == null) {
                    throw new IllegalArgumentException("Жанр с ID " + genre.getId() + " не найден");
                }
                updatedGenres.add(fullGenre);
            }
            film.setGenres(updatedGenres);
        }

        return film;
    }

    @Override
    public Optional<Film> updateFilm(Film film) throws ValidationException {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, " +
                "rating_id = ? WHERE id = ?";

        jdbcTemplate.update(
                sqlQuery,
                film.getTitle(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return Optional.of(film);
    }


    @Override
    public Optional<Film> getFilmById(Long id) {
        List<Film> films = jdbcTemplate.query("SELECT f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "l.USER_ID AS like_id, " +
                "mr.id AS mpa_id, " +
                "mr.name AS mpa_name, " +
                "g.id AS genre_id , " +
                "g.name AS genre_name " +
                "FROM films AS f " +
                "LEFT JOIN LIKES AS l ON (f.ID = l.FILM_ID) " +
                "LEFT JOIN RATING_MPA AS mr ON (f.RATING_MPA_ID  = mr.ID) " +
                "LEFT JOIN FILMS_GENRE AS fg ON (f.ID  = fg.film_id) " +
                "LEFT JOIN GENRES AS g ON (fg.genre_id = g.ID)" +
                "WHERE F.ID = ?;", filmMapper, id);
        if (films.isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(films.getFirst());
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = jdbcTemplate.query("SELECT f.id, " +
                "f.name, " +
                "f.description, " +
                "f.release_date, " +
                "f.duration, " +
                "l.USER_ID AS like_id, " +
                "mr.id AS mpa_id, " +
                "mr.name AS mpa_name, " +
                "g.id AS genre_id , " +
                "g.name AS genre_name " +
                "FROM films AS f " +
                "LEFT JOIN LIKES AS l ON (f.ID = l.FILM_ID) " +
                "LEFT JOIN RATING_MPA AS mr ON (f.RATING_MPA_ID  = mr.ID) " +
                "LEFT JOIN FILMS_GENRE AS fg ON (f.ID  = fg.film_id) " +
                "LEFT JOIN GENRES AS g ON (fg.genre_id = g.ID);", filmMapper);
        Set<Film> uniqueFilms = new TreeSet<>(Comparator.comparing(Film::getId));
        uniqueFilms.addAll(films);
        return new ArrayList<>(uniqueFilms);
    }

    @Override
    public void deleteFilm(Long id) {
        jdbcTemplate.update("DELETE FROM films WHERE id = ?", id);
    }

    @Override
    public void addLike(Long id, Long userId) {
        jdbcTemplate.update("INSERT INTO likes (film_id, user_id)values (?, ?);", id, userId);
    }

    @Override
    public void removeLike(Long id, Long userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND user_id = ?;", id, userId);
    }

    @Override
    public List<User> getLikes(Long filmId) {
        try {
            return jdbcTemplate.query("SELECT * FROM users WHERE id IN (SELECT user_id FROM likes WHERE film_id = ?)", new DataClassRowMapper<>(User.class), filmId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Film> getPopularFilms(Long count) {
        return jdbcTemplate.query(
                "SELECT ID, NAME, cnt_like " +
                        "FROM PUBLIC.FILMS f " +
                        "LEFT JOIN (select FILM_ID, COUNT(user_id) cnt_like from likes group by FILM_ID) l ON (f.id = l.FILM_ID) " +
                        "ORDER BY l.cnt_like DESC " +
                        "LIMIT ?", new DataClassRowMapper<>(Film.class), count);

    }

    @Override
    public void checkLikeExists(Long filmId, Long userId) {
        String sql = "SELECT COUNT(*) FROM likes WHERE film_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, filmId, userId);

        if (count > 0) {
            throw new AlreadyExistsException("Пользователь с id = " + userId + " уже поставил лайк фильму " + filmId);
        }
    }


}