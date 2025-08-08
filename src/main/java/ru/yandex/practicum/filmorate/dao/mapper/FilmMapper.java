package ru.yandex.practicum.filmorate.dao.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.dto.Genre;
import ru.yandex.practicum.filmorate.dto.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Component
public class FilmMapper implements RowMapper<Film> {

    Map<Long, Film> filmMap = new HashMap<>();

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long filmId = rs.getLong("id");
        Film film = filmMap.get(filmId);

        if (film == null) {
            film = new Film();
            film.setId(filmId);
            film.setTitle(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration"));
            film.setGenres(new HashSet<>());
            film.setLikes(new HashSet<>());

            MpaRating mpa = new MpaRating();
            mpa.setId(rs.getInt("mpa_id"));
            mpa.setName(rs.getString("mpa_name"));
            film.setMpa(mpa);


            filmMap.put(filmId, film);
        }
        long likeId = rs.getLong("like_id");
        if (likeId != 0) {
            film.getLikes().add(likeId);
        }

        long genreId = rs.getLong("genre_id");
        if (genreId != 0) {
            Genre genre = new Genre();
            genre.setId(genreId);
            genre.setName(rs.getString("genre_name"));
            film.getGenres().add(genre);
        }

        if (rs.isLast()) {
            filmMap.clear();
        }

        return film;
    }
}
