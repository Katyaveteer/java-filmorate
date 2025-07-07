package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@EqualsAndHashCode(of = {"id"})
public class Film {
    Long id;
    String name;
    String description;
    LocalDate releaseDate;
    int duration;
    private Set<Long> likes = new HashSet<>();

}
