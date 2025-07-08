package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"id"})
public class User {
    private final Set<Long> friends = new HashSet<>();
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

}
