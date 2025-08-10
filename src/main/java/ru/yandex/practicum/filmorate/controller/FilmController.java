package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;


@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @GetMapping
    public List<Film> findAll() {
        return service.getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        if (film.getId() != null) {
            throw new ValidationException("Id не должен передаваться при создании фильма");
        }
        return service.addFilm(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        return service.updateFilm(film)
                .orElseThrow(() -> new NotFoundException("Не удалось обновить фильм с id = " + film.getId()));
    }


    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable Long filmId) {
        return service.getFilmById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильм не найден с id = " + filmId));
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        service.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        service.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return service.getPopularFilms(count);
    }
}