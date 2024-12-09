package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;


@SpringBootTest
class FilmorateApplicationTests {
    FilmController filmController = new FilmController();
    UserController userController = new UserController();

    @BeforeAll
    static void startUp() {
        SpringApplication.run(FilmorateApplication.class);
    }

    @Test
    void createFilm() {
        Film film = new Film(1L, "Film name", "Description1", LocalDate.now(), Duration.ofMinutes(4));

        Assertions.assertNotNull(filmController.create(film));
    }

    @Test
    void getFilms() {
        System.out.println(filmController.getAll());
        Assertions.assertNotNull(filmController.getAll());
    }

    @Test
    void updateFilm() {
        Film film = Film.builder()
                .id(1L)
                .name("Film name")
                .description("Description1")
                .releaseDate(LocalDate.now())
                .duration(Duration.ofMinutes(5))
                .build();

        filmController.create(film);

        Film film2 = Film.builder()
                .id(1L)
                .name("New Film name")
                .description("New Description2")
                .releaseDate(LocalDate.now().minusYears(2))
                .duration(Duration.ofMinutes(15))
                .build();

        Assertions.assertNotNull(filmController.update(film2));
    }

    @Test
    void createUser() {
        User user = User.builder()
                .id(0L)
                .login("Login")
                .name("Name")
                .email("email@email.ru")
                .birthday(LocalDate.now().minusYears(20))
                .build();

        userController.create(user);
        Assertions.assertNotNull(userController.create(user));
    }

    @Test
    void getUsers() {
        System.out.println(userController.getAll());
        Assertions.assertNotNull(userController.getAll());
    }

    @Test
    void updateUser() {
        User user = User.builder()
                .id(0L)
                .login("Login")
                .name("Name")
                .email("email@email.ru")
                .birthday(LocalDate.now().minusYears(20))
                .build();

        userController.create(user);

        User user2 = User.builder()
                .id(1L)
                .login("New Login")
                .name("New Name")
                .email("new2email@email.ru")
                .birthday(LocalDate.now().minusYears(22))
                .build();

        Assertions.assertNotNull(userController.update(user2));
    }
}
