package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.dto.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAllGenres();

    Genre findGenreById(long id);

    List<Genre> getGenresByFilmId(long filmId);

}
