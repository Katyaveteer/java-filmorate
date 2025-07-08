package ru.yandex.practicum.filmorate.service;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;


import java.util.Collection;
import java.util.List;


@Getter
@Service
public class FilmService {

    private final FilmStorage filmStorage;


    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;

    }

    public Film create(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getById(Long id) {
        return filmStorage.getById(id);
    }

    public void delete(Long id) {
        filmStorage.delete(id);
    }

    public void addLike(Long filmId, Long userId) {
        try {
            filmStorage.addLike(filmId, userId);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    public void removeLike(Long filmId, Long userId) {

        try {
            filmStorage.removeLike(filmId, userId);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    public List<Film> getPopularFilms(int count) {

        return filmStorage.getPopularFilms(count);
    }


}
