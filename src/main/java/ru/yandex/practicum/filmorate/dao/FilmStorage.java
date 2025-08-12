package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.dto.User;

import java.util.List;
import java.util.Optional;


public interface FilmStorage {

    Film addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Optional<Film> getFilmById(Long id);

    List<Film> getAllFilms();

    void deleteFilm(Long id);

    void addLike(Long id, Long userId);

    void removeLike(Long id, Long userId);

    List<User> getLikes(Long filmId);

    List<Film> getPopularFilms(Long count);

    void checkLikeExists(Long filmId, Long userId);


}
