package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;


import java.time.Duration;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = {"id"})
public class Film {
    Long id;

    @NotBlank(message = "Название не может быть пустым")
    String name;
    @Size(max = 200, message = "Максимальная длина описания — 200 символов")
    String description;
    @NotNull(message = "Дата релиза обязательна")
    LocalDate releaseDate;
    @NotNull(message = "Продолжительность обязательна")
    @Positive(message = "Продолжительность должна быть положительной")

    Duration duration;

}

