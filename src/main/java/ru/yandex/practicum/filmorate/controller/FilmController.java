package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService service;

    @GetMapping
    public Collection<Film> findAll() {
        return service.getAllFilms();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        return service.addFilm(film);
    }

    @PutMapping
    public Optional<Film> update(@Valid @RequestBody Film film) {
        return service.updateFilm(film);
    }

    @GetMapping("/{filmId}")
    public Optional<Film> getFilm(@PathVariable Long filmId) {
        return service.getFilmById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLikeToFilm(@PathVariable Long filmId, @PathVariable Long userId) {
        service.addLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLikeFromFilm(@PathVariable Long filmId, @PathVariable Long userId) {
        service.removeLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Long count) {
        return service.getPopularFilms(count);
    }
}