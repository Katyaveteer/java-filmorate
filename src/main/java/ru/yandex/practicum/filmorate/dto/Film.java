package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;


@Setter
@Getter
@EqualsAndHashCode(of = "id")
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @NotNull

    private LocalDate releaseDate;
    @Positive
    private Integer duration;
    private Set<Long> likes;
    @NotNull
    private MpaRating mpa;
    private Set<Genre> genres = new LinkedHashSet<>();


}
