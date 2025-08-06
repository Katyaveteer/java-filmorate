package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.MpaDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.dao.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.dao.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.dao.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.dao.mapper.UserMapper;
import ru.yandex.practicum.filmorate.dto.Film;
import ru.yandex.practicum.filmorate.dto.Genre;
import ru.yandex.practicum.filmorate.dto.MpaRating;
import ru.yandex.practicum.filmorate.dto.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class,
        FilmDbStorage.class,
        GenreDbStorage.class,
        MpaDbStorage.class,
        UserMapper.class,
        FilmMapper.class,
        GenreMapper.class,
        MpaMapper.class
})
class FilmoRateApplicationTests {
    private final UserDbStorage userStorage;
    private final FilmDbStorage filmStorage;
    private final GenreDbStorage genreStorage;
    private final MpaDbStorage mpaStorage;

    @Test
    public void testFindUserById() {

        User testUser = new User();
        testUser.setEmail("test@example.com");
        testUser.setLogin("testLogin");
        testUser.setName("Test Name");
        testUser.setBirthday(LocalDate.of(1990, 1, 1));

        // Сохраняем пользователя
        User createdUser = userStorage.addUser(testUser);

        // Получаем пользователя по id
        Optional<User> userOptional = userStorage.getUserById(createdUser.getUserId());

        // Проверяем
        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("email", "test@example.com")
                );
    }

    @Test
    public void testCreateAndGetFilm() {
        MpaRating mpa = mpaStorage.findMpaById(1).orElseThrow();
        Film film = new Film();
        film.setTitle("Test Film");
        film.setDescription("Test Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
        film.setMpa(mpa);

        Film createdFilm = filmStorage.addFilm(film);
        Optional<Film> filmOptional = filmStorage.getFilmById(createdFilm.getFilmId());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(f ->
                        assertThat(f).hasFieldOrPropertyWithValue("title", "Test Film")
                );
    }

    @Test
    public void testAddAndGetFriends() {

        User user1 = createTestUser("user1@example.com", "user1");
        User user2 = createTestUser("user2@example.com", "user2");

        userStorage.addFriend(user1.getUserId(), user2.getUserId());

        List<User> friends = userStorage.getFriends(user1.getUserId());

        assertThat(friends).hasSize(1); // Ожидаем только одного друга
        assertThat(friends.getFirst().getUserId()).isEqualTo(user2.getUserId()); // Проверяем ID друга
    }

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = genreStorage.getAllGenres();

        assertThat(genres).isNotEmpty();
        assertThat(genres).extracting("name").contains("Комедия", "Драма");
    }

    @Test
    public void testGetAllMpa() {
        List<MpaRating> mpaList = mpaStorage.getAllMpa();

        assertThat(mpaList).isNotEmpty();
        assertThat(mpaList).extracting("name").contains("G", "PG");
    }

    private User createTestUser(String email, String login) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setName("Test User");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        return userStorage.addUser(user);
    }

}





