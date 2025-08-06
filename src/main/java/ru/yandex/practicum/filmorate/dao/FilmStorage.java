package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.dto.Film;

import java.util.List;
import java.util.Optional;


public interface FilmStorage {

    Film addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Optional<Film> getFilmById(Long id);

    List<Film> getAllFilms();

    void deleteFilm(Long id);

    void addLike(Long filmId, Long userId);

    void removeLike(Long filmId, Long userId);

    List<Film> getPopularFilms(int count);

}
