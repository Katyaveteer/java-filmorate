package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;


    @Override
    public User add(User user) {
        //проверка всех критериев
        //1. Электронная почта не может быть пустой и должна содержать символ @;
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String error = "Электронная почта не может быть пустой и должна содержать символ @";
            log.error("Ошибка создания пользователя: {}", error);
            throw new ValidationException(error);
        }

        //2. Логин не может быть пустым и содержать пробелы;
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
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

    @Override
    public User update(User newUser) {
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

        // Проверка уникальности email
        if (newUser.getEmail() != null && !newUser.getEmail().isEmpty()) {
            for (User user : users.values()) {
                if (!user.getId().equals(newUser.getId()) &&
                        user.getEmail().equals(newUser.getEmail())) {
                    String error = "Этот имейл уже используется";
                    log.error("Ошибка обновления пользователя: {}", error);
                    throw new ValidationException(error);
                }
            }
        }
        // Проверка email
        if (newUser.getEmail() == null || newUser.getEmail().isBlank() || !newUser.getEmail().contains("@")) {
            String error = "Электронная почта не может быть пустой и должна содержать символ @";
            log.error("Ошибка обновления пользователя: {}", error);
            throw new ValidationException(error);
        }

        // Проверка логина
        if (newUser.getLogin() == null || newUser.getLogin().isBlank() || newUser.getLogin().contains(" ")) {
            String error = "Логин не может быть пустым и содержать пробелы";
            log.error("Ошибка обновления пользователя: {}", error);
            throw new ValidationException(error);
        }

        // Проверка даты рождения
        if (newUser.getBirthday() == null || newUser.getBirthday().isAfter(LocalDate.now())) {
            String error = "Дата рождения должна быть указана и не может быть в будущем";
            log.error("Ошибка обновления пользователя: {}", error);
            throw new ValidationException(error);
        }
        // Обновление полей
        existingUser.setEmail(newUser.getEmail());
        existingUser.setLogin(newUser.getLogin());
        existingUser.setBirthday(newUser.getBirthday());

        // Имя может быть пустым - тогда используем логин
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            existingUser.setName(newUser.getLogin());
        } else {
            existingUser.setName(newUser.getName());
        }

        log.info("Обновлен пользователь с ID: {}", existingUser.getId());
        return existingUser;
    }

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User getById(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        return users.get(id);
    }
}
