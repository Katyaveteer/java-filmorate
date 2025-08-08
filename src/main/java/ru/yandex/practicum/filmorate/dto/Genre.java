package ru.yandex.practicum.filmorate.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Setter
@Getter
@RequiredArgsConstructor
public class Genre {
    private Long id;
    private String name;
}