package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;


@SpringBootTest
class FilmorateApplicationTests {
    InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    UserService userService = new UserService(inMemoryUserStorage);
    UserController userController = new UserController(inMemoryUserStorage, userService);
    FilmService filmService = new FilmService(inMemoryFilmStorage, inMemoryUserStorage);
    FilmController filmController = new FilmController(inMemoryFilmStorage, filmService);

    @BeforeAll
    static void startUp() {
        SpringApplication.run(FilmorateApplication.class);
    }

    @BeforeEach
    void beforeEach() {
        User user = User.builder()
                .id(1L)
                .login("Login")
                .name("Name")
                .email("email@email.ru")
                .birthday(LocalDate.now().minusYears(20))
                .build();
        userController.create(user);

        Film film = Film.builder()
                .id(1L)
                .name("Film name")
                .description("Description1")
                .releaseDate(LocalDate.now())
                .duration(300L)
                .build();
        filmController.create(film);
    }

    @Test
    void getFilms() {
        System.out.println(filmController.getAll());
        Assertions.assertNotNull(filmController.getAll());
    }

    @Test
    void updateFilm() {
        Film film2 = Film.builder()
                .id(1L)
                .name("New Film name")
                .description("New Description2")
                .releaseDate(LocalDate.now().minusYears(2))
                .duration(60L)
                .build();

        Assertions.assertNotNull(filmController.update(film2));
    }

    @Test
    void createUser() {
        User user = User.builder()
                .id(2L)
                .login("Login1")
                .name("Name1")
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
        User user2 = User.builder()
                .id(1L)
                .login("New Login")
                .name("New Name")
                .email("new2email@email.ru")
                .birthday(LocalDate.now().minusYears(22))
                .build();

        Assertions.assertNotNull(userController.update(user2));
    }

    @Test
    void getAllUsers() {
        Assertions.assertNotNull(userController.getAll());
    }

    @Test
    void getAllFilms() {
        Assertions.assertNotNull(filmController.getAll());
    }

    @Test
    void getUserAndFriends() {
        User user = User.builder()
                .id(1L)
                .login("Login")
                .name("Name")
                .email("email@email.ru")
                .birthday(LocalDate.now().minusYears(20))
                .build();
        userController.create(user);

        User user2 = User.builder()
                .id(2L)
                .login("New Login")
                .name("New Name")
                .email("new2email@email.ru")
                .birthday(LocalDate.now().minusYears(22))
                .build();
        userController.create(user2);

        userController.getUserById(1L);
        userController.addFriend(1L, 2L);

        userController.getUserFriends(1L);
    }

    @Test
    void likeAndDelikeFilm() {
        filmController.likeFilm(1L, 1L);
        filmController.deleteLike(1L, 1L);
    }
}
