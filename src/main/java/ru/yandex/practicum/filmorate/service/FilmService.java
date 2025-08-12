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

import java.time.LocalDate;
import java.time.Month;
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
        validateFilm(film);

        if (mpaStorage.getMpaById(film.getMpa().getId()) == null) {
            throw new NotFoundException("Указанный MPA  не найден");
        }
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            genreStorage.checkGenresExists(new ArrayList<>(film.getGenres()));
        }
        Film createdFilm = filmStorage.addFilm(film);
        log.info("Фильм {} успешно создан", createdFilm);
        return createdFilm;
    }

    public Optional<Film> updateFilm(Film film) {
        validateFilmId(film.getId());
        validateFilm(film);
        Optional<Film> updatedFilm = filmStorage.updateFilm(film);
        log.info("Фильм {} успешно обновлен", updatedFilm);
        return updatedFilm;

    }

    public Optional<Film> getFilmById(Long filmId) {
        validateFilmId(filmId);
        return Optional.ofNullable(filmStorage.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм с id = " + filmId + " не найден")));
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void addLike(Long filmId, Long userId) {
        validateFilmId(filmId);
        if (userStorage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь отсутсвует");
        }

        filmStorage.checkLikeExists(filmId, userId);
        filmStorage.addLike(filmId, userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(Long filmId, Long userId) {
        validateFilmId(filmId);
        if (userStorage.getUserById(userId).isEmpty()) {
            throw new NotFoundException("Пользователь с таким ID не найден!");
        }
        filmStorage.removeLike(filmId, userId);
        log.info("Пользователь {} удалил лайк у фильма {}", userId, filmId);
    }

    public List<Film> getPopularFilms(Long count) {
        return filmStorage.getPopularFilms(count);
    }

    public void deleteFilm(Long filmId) {
        validateFilmId(filmId);
        filmStorage.deleteFilm(filmId);
    }

    public List<User> getLikes(Long filmId) {
        validateFilmId(filmId);
        return new ArrayList<>(filmStorage.getLikes(filmId));
    }

    private void validateFilm(Film film) {
        if (film == null) {
            throw new ValidationException("Фильм не может быть null");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getReleaseDate() == null) {
            throw new ValidationException("Дата релиза обязательна");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Описание не может превышать 200 символов");
        }
    }

    private void validateFilmId(Long filmId) {
        if (filmId == null || filmId <= 0) {
            throw new ValidationException("Некорректный ID фильма");
        }
    }

}

