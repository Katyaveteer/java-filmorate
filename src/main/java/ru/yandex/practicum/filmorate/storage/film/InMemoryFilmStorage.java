package ru.yandex.practicum.filmorate.storage.film;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private final Map<Long, Set<Long>> likes = new HashMap<>();
    private long nextId = 1;

    @Override
    public Film create(Film film) {

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

    @Override
    public Film update(Film newFilm) {
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
        if (oldFilm == null) {
            String error = "Фильм с id = " + newFilm.getId() + " не найден";
            log.error("Ошибка обновления фильма: {}", error);
            throw new NotFoundException(error);
        }

        // Проверка названия
        if (newFilm.getName() == null || newFilm.getName().isBlank()) {
            String error = "Название не может быть пустым";
            log.error("Ошибка обновления фильма: {}", error);
            throw new ValidationException(error);
        }
        oldFilm.setName(newFilm.getName());


        // Проверка описания
        if (newFilm.getDescription() != null) {
            if (newFilm.getDescription().length() > 200) {
                String error = "Максимальная длина описания - 200 символов";
                log.error("Ошибка обновления фильма: {}", error);
                throw new ValidationException(error);
            }
            oldFilm.setDescription(newFilm.getDescription());
        }
        // Проверка даты релиза
        if (newFilm.getReleaseDate() != null) {
            if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))) {
                String error = "Дата релиза - не раньше 28 декабря 1895 года";
                log.error("Ошибка обновления фильма: {}", error);
                throw new ValidationException(error);
            }
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
        }

        // Проверка продолжительности
        if (newFilm.getDuration() <= 0) {
            String error = "Продолжительность фильма должна быть положительным числом";
            log.error("Ошибка обновления фильма: {}", error);
            throw new ValidationException(error);
        }
        oldFilm.setDuration(newFilm.getDuration());

        log.info("Обновлен фильм с ID: {}", oldFilm.getId());
        return oldFilm;
    }

    @Override
    public Collection<Film> findAll() {
        return films.values();
    }


    @Override
    public Film getById(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм не найден");
        }
        return films.get(id);
    }

}

