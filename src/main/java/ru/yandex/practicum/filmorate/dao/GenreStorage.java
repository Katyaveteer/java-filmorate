package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.dto.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreStorage {
    List<Genre> getAllGenres();

    Optional<Genre> findGenreById(long id);

    List<Genre> getGenresByFilmId(long filmId);

}
