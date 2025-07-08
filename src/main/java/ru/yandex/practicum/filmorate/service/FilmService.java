package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;


    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;

    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film newFilm) {
        return filmStorage.update(newFilm);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getById(Long id) {
        return filmStorage.getById(id);
    }

    public void addLike(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            String error = "Фильм с id" + filmId + "не найден";
            log.error("Ошибка создания лайка: {}", error);
            throw new NotFoundException(error);
        }
        if (userStorage.getById(userId) == null) {  // проверка пользователя
            String error = "Пользователь с id " + userId + " не найден";
            log.error("Ошибка создания лайка: {}", error);
            throw new NotFoundException(error);
        }
        film.getLikes().add(userId);
    }

    public void removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        if (film == null) {
            String error = "Фильм с id" + filmId + "не найден";
            log.error("Ошибка удаления лайка: {}", error);
            throw new NotFoundException(error);
        }
        if (userStorage.getById(userId) == null) {  // проверка пользователя
            String error = "Пользователь с id " + userId + " не найден";
            log.error("Ошибка удаления лайка: {}", error);
            throw new NotFoundException(error);
        }
        film.getLikes().remove(userId);

    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.findAll().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());

    }


}
