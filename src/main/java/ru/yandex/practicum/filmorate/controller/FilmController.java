package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;


    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;

    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Создание фильма: {}", film);
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Обновление фильма: {}", newFilm);
        return filmService.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        filmService.removeLike(filmId, userId);

    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

}

