package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    void findAllShouldReturnEmptyMapIfNothingAdded() {
        assertEquals(0, filmController.findAll().size());
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
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void createShouldThrowExceptionIfFilmNameIsEmpty() {
        Film film = new Film(1, "", "Comedy", LocalDate.of(2020, 10, 25), 120);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void createShouldThrowExceptionIfFilmDescriptionLengthIsLongerThen200Characters() {
        String description = "Nam quis nulla. Integer malesuada. In in enim a arcu imperdiet malesuada. Sed vel " +
                "lectus. Donec odio urna, tempus molestie, porttitor ut, iaculis quis, sem. Phasellus rhoncus. " +
                "Aenean id metus id velit ulla";
        assertEquals(205, description.length());
        Film film = new Film(1, "Film", description, LocalDate.of(2020, 10, 25), 120);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void createShouldThrowExceptionIfFilmReleaseDateIsEarlierAs28ThOfDecember1895() {
        Film film = new Film(1, "Film", "Comedy", LocalDate.of(1800, 10, 25), 120);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void createShouldThrowExceptionIfFilmDurationIsZero() {
        Film film = new Film(1, "Film", "Comedy", LocalDate.of(2020, 10, 25), 0);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void createShouldThrowExceptionIfFilmDurationIsNegative() {
        Film film = new Film(1, "Film", "Comedy", LocalDate.of(2020, 10, 25), -5);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }
    
    @Test
    void updateShouldThrowExceptionIfFilmIdIsNotExist() {
        Film film = new Film(50, "Film", "Horror", LocalDate.of(2020, 10, 25), 100);
        assertThrows(ValidationException.class, () -> filmController.update(film));
    }
}