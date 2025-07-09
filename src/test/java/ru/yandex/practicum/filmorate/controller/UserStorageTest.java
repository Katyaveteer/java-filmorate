package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserStorageTest {
    private User validUser;
    private InMemoryUserStorage storage;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setEmail("Email@Email");
        validUser.setLogin("Login");
        validUser.setName("Name");
        validUser.setBirthday(LocalDate.of(1997, Month.FEBRUARY, 8));
        storage = new InMemoryUserStorage();
    }

    @Test
    void createUserNotCharShouldFail() throws ValidationException {
        validUser.setEmail("aaa");
        Exception exception = assertThrows(ValidationException.class, () -> storage.create(validUser), "Электронная почта не может быть пустой и должна содержать символ @");
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
    }

    @Test
    void createUserEmptyLoginShouldFail() throws ValidationException {
        validUser.setLogin(" ");
        Exception exception = assertThrows(ValidationException.class, () -> storage.create(validUser), "Логин не может быть пустым и содержать пробелы");
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
    }

    @Test
    void createUserEmptyNameShouldSetLoginAsName() {
        validUser.setName(" ");
        User user = storage.create(validUser);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void createUserBirthdayFutureShouldFail() throws ValidationException {
        validUser.setBirthday(LocalDate.now().plusDays(1));
        Exception exception = assertThrows(ValidationException.class, () -> storage.create(validUser), "Дата рождения не может быть в будущем");
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    void createUserBirthdayPastShouldValid() throws ValidationException {
        validUser.setBirthday(LocalDate.now().minusDays(1));
        storage.create(validUser);
    }

    @Test
    void updateUserEmptyIdShouldFail() throws ValidationException {
        validUser.setId(null);
        Exception exception = assertThrows(ValidationException.class, () -> storage.update(validUser), "Id должен быть указан");
        assertEquals("Id должен быть указан", exception.getMessage());
    }

    @Test
    void updateUserEmailRepeatShouldFail() throws ValidationException {
        User user = new User();
        user.setEmail("Email2@Email");
        user.setLogin("Login2");
        user.setName("Name2");
        user.setBirthday(LocalDate.of(1998, Month.FEBRUARY, 10));
        storage.create(validUser);
        storage.create(user);
        user.setEmail(validUser.getEmail());
        Exception exception = assertThrows(ValidationException.class, () -> storage.update(user), "Этот имейл уже используется");
        assertEquals("Этот имейл уже используется", exception.getMessage());
    }


}
