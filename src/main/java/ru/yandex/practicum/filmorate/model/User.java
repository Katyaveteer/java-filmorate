package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.time.LocalDate;

@Data
@EqualsAndHashCode(of = {"id"})
public class User {
    Long id;
    String email;
    String login;
    String name;
    LocalDate birthday;

}
