package ru.yandex.practicum.filmorate.model;


import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(of = {"id"})
public class User {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

}
