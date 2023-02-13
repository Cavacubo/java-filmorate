package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController = new UserController();

    @Test
    void findAllShouldReturnEmptyMapIfNothingAdded() {
        assertEquals(0, userController.findAll().size());
        assertTrue(userController.findAll().isEmpty());
    }

    @Test
    void createHappyPath() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        assertEquals(user, userController.create(user));
        assertEquals(1, userController.findAll().size());
    }

    @Test
    void createShouldUseLoginIfUserNameIsNull() {
        User user = new User(1, "email@gmail.com", "user", null, LocalDate.of(1990, 12, 10));
        userController.create(user);
        assertEquals("user", user.getName());
    }

    @Test
    void createShouldUseLoginIfUserNameIsEmpty() {
        User user = new User(1, "email@gmail.com", "user", "", LocalDate.of(1990, 12, 10));
        userController.create(user);
        assertEquals("user", user.getName());
    }

    @Test
    void createShouldThrowExceptionIfUserEmailIsNull() {
        User user = new User(1, null, "user", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userController.create(user));
    }

    @Test
    void createShouldThrowExceptionIfUserEmailDoesNotContainAt() {
        User user = new User(1, "email.gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userController.create(user));
    }

    @Test
    void createShouldThrowExceptionIfUserLoginIsNull() {
        User user = new User(1, "email@gmail.com", null, "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userController.create(user));
    }

    @Test
    void createShouldThrowExceptionIfUserLoginIsEmpty() {
        User user = new User(1, "email@gmail.com", "", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userController.create(user));
    }

    @Test
    void createShouldThrowExceptionIfUserLoginContainsSpacesAtTheBeginning() {
        User user = new User(1, "email@gmail.com", "  user", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userController.create(user));
    }

    @Test
    void createShouldThrowExceptionIfUserLoginContainsSpacesAtTheEnd() {
        User user = new User(1, "email@gmail.com", "user ", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userController.create(user));
    }

    @Test
    void createShouldThrowExceptionIfUserLoginContainsSpacesInTheMiddle() {
        User user = new User(1, "email@gmail.com", "us  er", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userController.create(user));
    }

    @Test
    void createShouldThrowExceptionIfUserLoginContainsOnlySpaces() {
        User user = new User(1, "email@gmail.com", "   ", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userController.create(user));
    }

    @Test
    void createShouldThrowExceptionIfUserBirthdayIsInTheFuture() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(2023, 10, 15));
        assertThrows(BadRequestException.class, () -> userController.create(user));
    }

    @Test
    void updateShouldThrowExceptionIfFilmIdIsNotExist() {
        User user = new User(55, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(NotFoundException.class, () -> userController.update(user));
    }
}