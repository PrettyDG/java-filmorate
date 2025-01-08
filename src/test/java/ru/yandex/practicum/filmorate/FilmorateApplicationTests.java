package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.dal.UserDbStorage;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class})
class FilmorateApplicationTests {
    private final UserDbStorage userStorage;
    private final UserController userController;
    private final FilmController filmController;
    private final FilmDbStorage filmDbStorage;

    @BeforeAll
    static void startUp() {
        SpringApplication.run(FilmorateApplication.class);
    }

    @Test
    public void testFindUserById() {
        User user1 = User.builder()
                .id(Long.parseLong("1"))
                .login("Login")
                .name("Name")
                .email("email@email.ru")
                .birthday(LocalDate.now().minusYears(20))
                .build();
        userController.create(user1);

        Film film1 = Film.builder()
                .id(Long.parseLong("1"))
                .name("Film name")
                .description("Description1")
                .releaseDate(LocalDate.now())
                .duration(300L)
                .mpa(new Mpa())
                .build();
        filmController.create(film1);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(Long.parseLong("1")));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testIsFilmNameAlreadyExist() {
        Film film = Film.builder()
                .name("Unique Name")
                .description("Some Description")
                .releaseDate(LocalDate.of(2023, 6, 6))
                .duration(125L)
                .mpa(new Mpa())
                .build();

        filmDbStorage.createFilm(film);

        boolean exists = filmDbStorage.isFilmNameAlreadyExist("Unique Name");

        assertThat(exists).isTrue();
    }

    @Test
    public void testIsFilmAlreadyCreatedById() {
        Film film = Film.builder()
                .name("Film for ID Check")
                .description("Description for ID Check")
                .releaseDate(LocalDate.of(2023, 5, 5))
                .duration(115L)
                .mpa(new Mpa())
                .build();

        filmDbStorage.createFilm(film);

        boolean exists = filmDbStorage.isFilmAlreadyCreatedById(film.getId());

        assertThat(exists).isTrue();
    }

    @Test
    public void testUpdateFilm() {
        Film film = Film.builder()
                .name("Original Film")
                .description("Original Description")
                .releaseDate(LocalDate.of(2022, 4, 14))
                .duration(90L)
                .mpa(new Mpa())
                .build();

        filmDbStorage.createFilm(film);

        film.setName("Updated Film");
        film.setDescription("Updated Description");

        filmDbStorage.updateFilm(film);

        Film updatedFilm = filmDbStorage.getFilmById(film.getId());

        assertThat(updatedFilm).isNotNull();
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("name", "Updated Film");
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("description", "Updated Description");
    }

    @Test
    public void testGetFilmById() {
        Film film = Film.builder()
                .name("Test Film 2")
                .description("Test Description 2")
                .releaseDate(LocalDate.of(2021, 5, 10))
                .duration(100L)
                .mpa(new Mpa())
                .build();

        filmDbStorage.createFilm(film);

        Film retrievedFilm = filmDbStorage.getFilmById(film.getId());

        assertThat(retrievedFilm).isNotNull();
        assertThat(retrievedFilm).hasFieldOrPropertyWithValue("name", "Test Film 2");
    }

    @Test
    void testCreateUser() {
        User user = User.builder()
                .email("user1@example.com")
                .login("user1")
                .name("User One")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        userStorage.createUser(user);
        Optional<User> retrievedUser = Optional.ofNullable(userStorage.getUserById(user.getId()));

        assertThat(retrievedUser)
                .isPresent()
                .hasValueSatisfying(u -> assertThat(u.getEmail()).isEqualTo("user1@example.com"));
    }

    @Test
    void testGetUserById() {
        User user = User.builder()
                .email("user2@example.com")
                .login("user2")
                .name("User Two")
                .birthday(LocalDate.of(1992, 2, 2))
                .build();

        userStorage.createUser(user);
        User retrievedUser = userStorage.getUserById(user.getId());

        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getEmail()).isEqualTo("user2@example.com");
    }

    @Test
    void testIsUserAlreadyCreatedById() {
        User user = User.builder()
                .email("user5@example.com")
                .login("user5")
                .name("User Five")
                .birthday(LocalDate.of(1995, 5, 5))
                .build();

        userStorage.createUser(user);
        boolean exists = userStorage.isUserAlreadyCreatedById(user.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void testIsUserEmailAlreadyExist() {
        User user = User.builder()
                .email("user6@example.com")
                .login("user6")
                .name("User Six")
                .birthday(LocalDate.of(1996, 6, 6))
                .build();

        userStorage.createUser(user);
        boolean exists = userStorage.isUserEmailAlreadyExist("user6@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    void testUpdateUser() {
        User user = User.builder()
                .email("user7@example.com")
                .login("user7")
                .name("User Seven")
                .birthday(LocalDate.of(1997, 7, 7))
                .build();

        userStorage.createUser(user);

        User updatedUser = User.builder()
                .email("updated@example.com")
                .login("updatedUser")
                .name("Updated User")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();

        userStorage.updateUser(user.getId(), updatedUser);

        User retrievedUser = userStorage.getUserById(user.getId());

        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getEmail()).isEqualTo("updated@example.com");
        assertThat(retrievedUser.getLogin()).isEqualTo("updatedUser");
    }
}
