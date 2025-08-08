package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dao.UserStorage;
import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.dto.User;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;
    private final UserStorage userStorage;

    public Film addFilm(Film film) {
        if (mpaStorage.getMpaById(film.getMpa().getId()) == null) {
            throw new ValidationException("Указанный MPA  не найден");
        }
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            genreStorage.checkGenresExists(new ArrayList<>(film.getGenres()));
        }
        log.info("Фильм {} создан", film);
        return filmStorage.addFilm(film);
    }

    public Optional<Film> updateFilm(Film film) {
        if (film.getId() == null) {
            log.info("Id должен быть указан");
            throw new NullPointerException("Id должен быть указан");
        }
        if (filmStorage.getFilmById(film.getId()).isPresent()) {
            return filmStorage.updateFilm(film);
        }
        log.info("Не найден фильм");
        throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
    }

    public Optional<Film> getFilmById(Long filmId) {
        if (filmStorage.getFilmById(filmId).isPresent()) {
            return filmStorage.getFilmById(filmId);
        }
        throw new NotFoundException("Фильм с id = " + filmId + " не найден");
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            throw new NotFoundException("Фильм отсуствует");
        } else {
            if (userStorage.getUserById(userId).isEmpty()) {
                throw new NotFoundException("Пользователь отсутсвует");
            }
        }
        filmStorage.checkLikeExists(filmId, userId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            throw new NotFoundException("Фильм с таким ID не найден!");
        } else if (userStorage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким ID не найден!");
        }
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getPopularFilms(Long count) {
        return filmStorage.getPopularFilms(count);
    }

    public void deleteFilm(Long id) {
        if (filmStorage.getFilmById(id).isEmpty()) {
            throw new NotFoundException("Фильм с таким ID не найден!");
        }
        filmStorage.deleteFilm(id);
    }

    public List<User> getLikes(Long filmId) {
        if (filmStorage.getFilmById(filmId).isEmpty()) {
            throw new NotFoundException("Фильм с таким ID не найден!");
        }
        return new ArrayList<>(filmStorage.getLikes(filmId));
    }

}

