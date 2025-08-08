package ru.yandex.practicum.filmorate.dao;


import ru.yandex.practicum.filmorate.dto.Genre;

import java.util.List;


public interface GenreStorage {

    List<Genre> getAll();

    Genre getById(Long id);

    List<Genre> getGenresOfFilm(Long id);

    boolean checkGenresExists(List<Genre> genres);

}
