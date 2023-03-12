package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));

    @Test
    void findAllShouldReturnEmptyMapIfNothingAdded() {
        assertEquals(0, filmController.findAll().size());
    }

    @Test
    void findFilmByIdHappyPath() {
        Film film1 = new Film(1, "Film1", "Comedy", LocalDate.of(2020, 10, 25), 120);
        filmController.create(film1);
        assertEquals(1, filmController.findFilmById(1).getId());
    }

    @Test
    void findFilmByIdShouldThrowExceptionIfFilmIdDoesNotExist() {
        assertThrows(NotFoundException.class, () -> filmController.findFilmById(9999));
    }

    @Test
    void createHappyPath() {
        Film film1 = new Film(1, "Film1", "Comedy", LocalDate.of(2020, 10, 25), 120);
        assertEquals(film1, filmController.create(film1));
        assertEquals(1, filmController.findAll().size());
    }

    @Test
    void createShouldThrowExceptionIfFilmNameIsNull() {
        Film film = new Film(1, null, "Comedy", LocalDate.of(2020, 10, 25), 120);
        assertThrows(BadRequestException.class, () -> filmController.create(film));
    }

    @Test
    void createShouldThrowExceptionIfFilmNameIsEmpty() {
        Film film = new Film(1, "", "Comedy", LocalDate.of(2020, 10, 25), 120);
        assertThrows(BadRequestException.class, () -> filmController.create(film));
    }

    @Test
    void createShouldThrowExceptionIfFilmDescriptionLengthIsLongerThen200Characters() {
        String description = "Nam quis nulla. Integer malesuada. In in enim a arcu imperdiet malesuada. Sed vel " +
                "lectus. Donec odio urna, tempus molestie, porttitor ut, iaculis quis, sem. Phasellus rhoncus. " +
                "Aenean id metus id velit ulla";
        assertEquals(205, description.length());
        Film film = new Film(1, "Film", description, LocalDate.of(2020, 10, 25), 120);
        assertThrows(BadRequestException.class, () -> filmController.create(film));
    }

    @Test
    void createShouldThrowExceptionIfFilmReleaseDateIsEarlierAs28ThOfDecember1895() {
        Film film = new Film(1, "Film", "Comedy", LocalDate.of(1800, 10, 25), 120);
        assertThrows(BadRequestException.class, () -> filmController.create(film));
    }

    @Test
    void createShouldThrowExceptionIfFilmDurationIsZero() {
        Film film = new Film(1, "Film", "Comedy", LocalDate.of(2020, 10, 25), 0);
        assertThrows(BadRequestException.class, () -> filmController.create(film));
    }

    @Test
    void createShouldThrowExceptionIfFilmDurationIsNegative() {
        Film film = new Film(1, "Film", "Comedy", LocalDate.of(2020, 10, 25), -5);
        assertThrows(BadRequestException.class, () -> filmController.create(film));
    }

    @Test
    void updateShouldThrowExceptionIfFilmIdIsNotExist() {
        Film film = new Film(50, "Film", "Horror", LocalDate.of(2020, 10, 25), 100);
        assertThrows(NotFoundException.class, () -> filmController.update(film));
    }

    @Test
    void addLike() {
        Film film1 = new Film(1, "Film1", "Comedy", LocalDate.of(2020, 10, 25), 120);
        filmController.create(film1);
        filmController.addLike(1, 5);
        assertEquals(1, film1.getLikes().size());
    }

    @Test
    void deleteLike() {
        Film film1 = new Film(1, "Film1", "Comedy", LocalDate.of(2020, 10, 25), 120);
        filmController.create(film1);
        filmController.addLike(1, 5);
        filmController.deleteLike(1, 5);
        assertEquals(0, film1.getLikes().size());
    }

    @Test
    void getPopularFilms() {
        Film film1 = new Film(1, "Film1", "Comedy", LocalDate.of(2020, 10, 25), 120);
        filmController.create(film1);
        filmController.addLike(1, 5);
        Film film2 = new Film(2, "Film", "Horror", LocalDate.of(2020, 10, 25), 100);
        filmController.create(film2);
        filmController.addLike(2, 1);
        filmController.addLike(2, 3);
        filmController.addLike(2, 5);
        assertEquals(List.of(film2, film1), filmController.findMostPopularFilms(2));
    }
}