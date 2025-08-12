package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaStorage;
import ru.yandex.practicum.filmorate.dto.MpaRating;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage storage;

    public List<MpaRating> getAll() {
        return storage.getAllMpa();
    }

    public MpaRating getById(Long id) {
        if (storage.getMpaById(id) == null) {
            throw new NotFoundException("Рейтинга с таким id = " + id + " нет");
        } else {
            return storage.getMpaById(id);
        }

    }
}