package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmStorage;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.dto.Genre;
import ru.yandex.practicum.filmorate.dto.MpaRating;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final GenreStorage genreStorage;
    private final MpaStorage mpaStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       UserService userService,
                       GenreStorage genreStorage,
                       MpaStorage mpaStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmStorage.getAllFilms();
        films.forEach(this::loadFilmData);
        return films;
    }

    public Film getFilmById(Long id) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + id + " не найден"));
        loadFilmData(film);
        return film;
    }

    public Film addFilm(Film film) {
        validateFilm(film);
        Film createdFilm = filmStorage.addFilm(film);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            filmStorage.updateFilmGenres(film);
        }
        return getFilmById(createdFilm.getFilmId());
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        getFilmById(film.getFilmId()); // Проверка существования
        filmStorage.updateFilm(film)
                .orElseThrow(() -> new NotFoundException("Фильм с id=" + film.getFilmId() + " не найден"));
        filmStorage.updateFilmGenres(film);
        return getFilmById(film.getFilmId());
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
        List<Film> films = filmStorage.getPopularFilms(count);
        films.forEach(this::loadFilmData);
        return films;
    }

    public List<Genre> getFilmGenres(Long filmId) {
        getFilmById(filmId); // Проверка существования фильма
        return filmStorage.getFilmGenres(filmId);
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(Integer id) {
        return genreStorage.findGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id=" + id + " не найден"));
    }

    public List<MpaRating> getAllMpaRatings() {
        return mpaStorage.getAllMpa();
    }

    public MpaRating getMpaRatingById(Integer id) {
        return mpaStorage.findMpaById(id)
                .orElseThrow(() -> new NotFoundException("Рейтинг MPA с id=" + id + " не найден"));
    }

    private void loadFilmData(Film film) {
        film.setGenres(new HashSet<>(filmStorage.getFilmGenres(film.getFilmId())));
        film.setLikes(new HashSet<>(filmStorage.getFilmLikes(film.getFilmId())));
        if (film.getMpa() != null) {
            film.setMpa(mpaStorage.findMpaById(film.getMpa().getMpaId())
                    .orElseThrow(() -> new NotFoundException("MPA рейтинг не найден")));
        }
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            throw new ValidationException("Дата релиза не может быть раньше " + CINEMA_BIRTHDAY);
        }
        if (film.getMpa() == null || film.getMpa().getMpaId() == null) {
            throw new ValidationException("Фильм должен содержать рейтинг MPA");
        }
        // Проверка существования MPA рейтинга
        mpaStorage.findMpaById(film.getMpa().getMpaId())
                .orElseThrow(() -> new NotFoundException("MPA рейтинг с id=" + film.getMpa().getMpaId() + " не найден"));
    }
}