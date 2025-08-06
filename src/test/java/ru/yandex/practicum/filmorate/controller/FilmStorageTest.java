package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class FilmStorageTest {

    private Film validFilm;
    private InMemoryFilmStorage storage;

    @BeforeEach
    void setUp() {
        validFilm = new Film();
        validFilm.setName("Valid Film");
        validFilm.setDescription("Normal description");
        validFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        validFilm.setDuration(120);
        storage = new InMemoryFilmStorage();
    }

    @Test
    void createFilmEmptyNameShouldFail() throws ValidationException {
        validFilm.setName("");
        Exception exception = assertThrows(ValidationException.class, () -> storage.create(validFilm), "Название не может быть пустым");
        assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void createFilmDescription201ShouldFail() throws ValidationException {
        validFilm.setDescription(buildString(201));
        Exception exception = assertThrows(ValidationException.class, () -> storage.create(validFilm), "Максимальная длина описания - 200 символов");
        assertEquals("Максимальная длина описания - 200 символов", exception.getMessage());
    }

    @Test
    void createFilmDescription200ShouldValid() throws ValidationException {
        validFilm.setDescription(buildString(200));
        storage.create(validFilm);
    }

    private String buildString(int length) {
        char[] chars = new char[length];
        Arrays.fill(chars, 'a');
        return new String(chars);
    }

    @Test
    void createFilmData27_12_1895ShouldFail() throws ValidationException {
        validFilm.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        Exception exception = assertThrows(ValidationException.class, () -> storage.create(validFilm), "Дата релиза - не раньше 28 декабря 1895 года");
        assertEquals("Дата релиза - не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void createFilmData28_12_1895ShouldValid() throws ValidationException {
        validFilm.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 28));
        storage.create(validFilm);
    }


    @Test
    void createFilmDurationIsNegativeShouldFail() throws ValidationException {
        validFilm.setDuration(-1);
        Exception exception = assertThrows(ValidationException.class, () -> storage.create(validFilm), "Продолжительность фильма должна быть положительным числом");
        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }

    @Test
    void updateFilmEmptyIdShouldFail() throws ValidationException {
        validFilm.setId(null);
        Exception exception = assertThrows(ValidationException.class, () -> storage.update(validFilm), "Id должен быть указан");
        assertEquals("Id должен быть указан", exception.getMessage());
    }

    @Test
    void updateFilmNewNameShouldUpdate() {
        Film createFilm = storage.create(validFilm);
        createFilm.setName("Update name");
        storage.update(createFilm);
        assertEquals("Update name", createFilm.getName());

    }
}

