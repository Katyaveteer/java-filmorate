package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        getFilmById(film.getFilmId()); // Проверка существования
        return filmStorage.updateFilm(film)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + film.getFilmId() + " не найден"));
    }

    public void deleteFilm(Long id) {
        getFilmById(id); // Проверка существования
        filmStorage.deleteFilm(id);
        log.info("Фильм с id={} удален", id);
    }

    public void addLike(Long filmId, Long userId) {
        getFilmById(filmId);
        userService.getUserById(userId);
        filmStorage.addLike(filmId, userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        getFilmById(filmId);
        userService.getUserById(userId);
        filmStorage.removeLike(filmId, userId);
        log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);
    }

    public List<Film> getPopularFilms(int count) {
        if (count <= 0) {
            throw new ValidationException("Параметр count должен быть положительным");
        }
        return filmStorage.getPopularFilms(count);
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            throw new ValidationException("Дата релиза не может быть раньше " + CINEMA_BIRTHDAY);
        }
    }
}