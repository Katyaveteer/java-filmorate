package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    @GetMapping
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {

        //проверка всех критериев
        //1. Название не может быть пустым
        if (film.getName() == null || film.getName().isBlank()) {
            String error = "Название не может быть пустым";
            log.error("Ошибка создания фильма: {}", error);
            throw new ValidationException(error);
        }

        //2. Максимальнаядлна описания - 200 символов
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            String error = "Максимальная длина описания - 200 символов";
            log.error("Ошибка создания фильма: {}", error);
            throw new ValidationException(error);
        }

        //3. Дата релиза - не раньше 28 декабря 1895 года
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
            String error = "Дата релиза - не раньше 28 декабря 1895 года";
            log.error("Ошибка создания фильма: {}", error);
            throw new ValidationException(error);
        }

        //4. Продолжительность фильма должна быть положительным числом
        if (film.getDuration() <= 0) {
            String error = "Продолжительность фильма должна быть положительным числом";
            log.error("Ошибка создания фильма: {}", error);
            throw new ValidationException(error);

        }
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Создан новый фильм с ID: {}", film.getId());
        return film;

    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        if (newFilm.getId() == null) {
            String error = "Id должен быть указан";
            log.error("Ошибка обновления фильма: {}", error);
            throw new ValidationException(error);
        }
        if (!films.containsKey(newFilm.getId())) {
            String error = "Фильм с id = " + newFilm.getId() + " не найден";
            log.error("Ошибка обновления фильма: {}", error);
            throw new NotFoundException(error);
        }

        Film oldFilm = films.get(newFilm.getId());

        if (newFilm.getName() != null && !newFilm.getName().isBlank()) {
            oldFilm.setName(newFilm.getName());
        }

        if (newFilm.getDescription() != null) {
            if (newFilm.getDescription().length() > 200) {
                String error = "Максимальная длина описания - 200 символов";
                log.error("Ошибка обновления фильма: {}", error);
                throw new ValidationException(error);
            }
            oldFilm.setDescription(newFilm.getDescription());
        }

        if (newFilm.getReleaseDate() != null) {
            if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
                String error = "Дата релиза - не раньше 28 декабря 1895 года";
                log.error("Ошибка обновления фильма: {}", error);
                throw new ValidationException(error);
            }
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }

        if (newFilm.getDuration() > 0) {
            oldFilm.setDuration(newFilm.getDuration());
        }

        log.info("Обновлен фильм с ID: {}", oldFilm.getId());
        return oldFilm;
    }

}

