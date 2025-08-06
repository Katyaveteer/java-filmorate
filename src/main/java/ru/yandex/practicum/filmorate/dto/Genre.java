package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class Genre {
    private Integer genreId;
    private String name;
}