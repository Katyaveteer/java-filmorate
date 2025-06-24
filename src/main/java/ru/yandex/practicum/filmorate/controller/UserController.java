package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {

        //проверка всех критериев
        //1. Электронная почта не может быть пустой и должна содержать символ @;
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String error = "Электронная почта не может быть пустой и должна содержать символ @";
            log.error("Ошибка создания пользователя: {}", error);
            throw new ValidationException(error);
        }

        //2. Логин не может быть пустым и содержать пробелы;
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            String error = "Логин не может быть пустым и содержать пробелы";
            log.error("Ошибка создания пользователя: {}", error);
            throw new ValidationException(error);
        }

        //3. Имя для отображения может быть пустым — в таком случае будет использован логин;
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        //4. Дата рождения не может быть в будущем.
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String error = "Дата рождения не может быть в будущем";
            log.error("Ошибка создания пользователя: {}", error);
            throw new ValidationException(error);

        }
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;

    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        if (newUser.getId() == null) {
            String error = "Id должен быть указан";
            log.error("Ошибка обновления пользователя: {}", error);
            throw new ValidationException(error);
        }
        User existingUser = users.get(newUser.getId());
        if (existingUser == null) {
            String error = "Пользователь с id = " + newUser.getId() + " не найден";
            log.error("Ошибка обновления пользователя: {}", error);
            throw new NotFoundException(error);
        }
        if (newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
            for (User user : users.values()) {
                if (!user.getId().equals(newUser.getId()) &&
                        user.getEmail().equals(newUser.getEmail())) {
                    String error = "Этот имейл уже используется";
                    log.error("Ошибка обновления пользователя: {}", error);
                    throw new ValidationException(error);
                }
            }
            existingUser.setEmail(newUser.getEmail());
        }
        if (newUser.getBirthday() != null && !newUser.getBirthday().isAfter(LocalDate.now())) {
            existingUser.setBirthday(newUser.getBirthday());
        }
        if (newUser.getName() != null) {
            existingUser.setName(newUser.getName());
        } else {
            existingUser.setName(newUser.getEmail());
        }

        if (newUser.getLogin() != null) {
            existingUser.setLogin(newUser.getLogin());
        }
        return existingUser;


    }
}

