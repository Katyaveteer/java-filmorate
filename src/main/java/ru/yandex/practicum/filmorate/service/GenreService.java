package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreStorage;
import ru.yandex.practicum.filmorate.dto.Genre;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage storage;

    public List<Genre> getAll() {
        return storage.getAll();
    }

    public Genre getById(Long id) {
        if (storage.getById(id) == null) {
            throw new NotFoundException("Жанра с таким id = " + id + " нет");
        } else {
            return storage.getById(id);
        }

    }
}

