package ru.yandex.practicum.filmorate.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class MpaRating {
    private Integer id;
    private String name;
}
