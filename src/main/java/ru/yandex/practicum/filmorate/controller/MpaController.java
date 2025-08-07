package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dto.MpaRating;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaStorage mpaStorage;

    @GetMapping
    public List<MpaRating> getAllMpa() {
        log.info("Получение всего рейтинга");
        return mpaStorage.getAllMpa();
    }


    @GetMapping("/{id}")
    public Optional<MpaRating> getMpaById(@PathVariable long id) {
        log.info("Получение  рейтинга по id");
        return mpaStorage.findMpaById(id);

    }
}