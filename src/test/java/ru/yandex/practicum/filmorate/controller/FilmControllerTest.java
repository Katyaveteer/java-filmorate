package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class FilmControllerTest {

    private Film validFilm;
    private FilmController controller;

    @BeforeEach
    void setUp() {
        validFilm = new Film();
        validFilm.setName("Valid Film");
        validFilm.setDescription("Normal description");
        validFilm.setReleaseDate(LocalDate.of(2000, 1, 1));
        validFilm.setDuration(Duration.ofMinutes(120));
        controller = new FilmController();
    }

    @Test
    void createFilmEmptyNameShouldFail() throws ValidationException {
        validFilm.setName("");
        Exception exception = assertThrows(ValidationException.class, () -> controller.create(validFilm), "Название не может быть пустым");
        assertEquals("Название не может быть пустым", exception.getMessage());
    }

    @Test
    void createFilmDescription201ShouldFail() throws ValidationException {
        validFilm.setDescription(buildString(201));
        Exception exception = assertThrows(ValidationException.class, () -> controller.create(validFilm), "Максимальная длна описания - 200 символов");
        assertEquals("Максимальная длина описания - 200 символов", exception.getMessage());
    }

    @Test
    void createFilmDescription200ShouldValid() throws ValidationException {
        validFilm.setDescription(buildString(200));
        controller.create(validFilm);
    }

    private String buildString(int length) {
        char[] chars = new char[length];
        Arrays.fill(chars, 'a');
        return new String(chars);
    }

    @Test
    void createFilmData27_12_1895ShouldFail() throws ValidationException {
        validFilm.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 27));
        Exception exception = assertThrows(ValidationException.class, () -> controller.create(validFilm), "Дата релиза - не раньше 28 декабря 1895 года");
        assertEquals("Дата релиза - не раньше 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void createFilmData28_12_1895ShouldValid() throws ValidationException {
        validFilm.setReleaseDate(LocalDate.of(1895, Month.DECEMBER, 28));
        controller.create(validFilm);
    }


    @Test
    void createFilmDurationIsNegativeShouldFail() throws ValidationException {
        validFilm.setDuration(Duration.ofSeconds(-600));
        Exception exception = assertThrows(ValidationException.class, () -> controller.create(validFilm), "Продолжительность фильма должна быть положительным числом");
        assertEquals("Продолжительность фильма должна быть положительным числом", exception.getMessage());
    }

    @Test
    void updateFilmEmptyIdShouldFail() throws ValidationException {
        validFilm.setId(null);
        Exception exception = assertThrows(ValidationException.class, () -> controller.update(validFilm), "Id должен быть указан");
        assertEquals("Id должен быть указан", exception.getMessage());
    }

    @Test
    void updateFilmNewNameShouldUpdate() {
        Film createFilm = controller.create(validFilm);
        createFilm.setName("Update name");
        Film newFilm = controller.update(createFilm);
        assertEquals("Update name", newFilm.getName());

    }
}

