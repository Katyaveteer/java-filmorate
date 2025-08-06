package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class MpaRating {
    private Integer mpaId;
    private String name;
    private String description;
}
