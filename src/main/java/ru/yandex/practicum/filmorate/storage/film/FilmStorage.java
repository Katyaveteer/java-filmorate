package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {

    Film add(Film film);

    Film update(Film newFilm);

    Collection<Film> findAll();

    Film getById(Long id);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    void delete(Long id);

    List<Film> getPopularFilms(int count);
}
