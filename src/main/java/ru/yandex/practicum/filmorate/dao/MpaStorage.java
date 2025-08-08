package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.dto.MpaRating;

import java.util.List;

public interface MpaStorage {

    List<MpaRating> getAllMpa();

    MpaRating getMpaById(long id);

    MpaRating getMpaOfFilm(Long id);
}
