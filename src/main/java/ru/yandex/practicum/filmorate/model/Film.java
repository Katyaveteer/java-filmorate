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
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;

}
