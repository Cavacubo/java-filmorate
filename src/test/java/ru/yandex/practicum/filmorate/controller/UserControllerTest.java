package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    @Test
    void findAllShouldReturnEmptyMapIfNothingAdded() {
        assertEquals(0, userController.findAll().size());
        assertTrue(userController.findAll().isEmpty());
    }

    @Test
    void findUserByIdHappyPath() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        userController.create(user);
        assertEquals(1, userController.findUser(1).getId());
        assertEquals("Jane", userController.findUser(1).getName());
    }

    @Test
    void findUserByIdShouldThrowExceptionIfUserDoesNotExist() {
        assertThrows(NotFoundException.class, () -> userController.findUser(5555));
    }

    @Test
    void createUserHappyPath() {
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

    @Test
    void addNewFriend() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        userController.create(user);
        User user2 = new User(2, "email2@gmail.com", "user2", "Kate", LocalDate.of(1980, 12, 10));
        userController.create(user2);
        userController.addNewFriend(1, 2);
        assertEquals(Set.of(2), user.getFriends());
    }

    @Test
    void deleteFriend() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        userController.create(user);
        User user2 = new User(2, "email2@gmail.com", "user2", "Kate", LocalDate.of(1980, 12, 10));
        userController.create(user2);
        userController.addNewFriend(1, 2);
        userController.deleteFriend(1, 2);
        assertEquals(Set.of(), user.getFriends());
    }

    @Test
    void getCommonFriendsList() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        userController.create(user);
        User user2 = new User(2, "email2@gmail.com", "user2", "Kate", LocalDate.of(1980, 12, 10));
        userController.create(user2);
        User user3 = new User(3, "email3@gmail.com", "user3", "Lukas", LocalDate.of(1985, 12, 10));
        userController.create(user3);
        userController.addNewFriend(1, 3);
        userController.addNewFriend(2, 3);
        assertEquals(List.of(user3), userController.findCommonFriends(1, 2));
    }

    @Test
    void getUserFriendsList() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        userController.create(user);
        User user2 = new User(2, "email2@gmail.com", "user2", "Kate", LocalDate.of(1980, 12, 10));
        userController.create(user2);
        User user3 = new User(3, "email3@gmail.com", "user3", "Lukas", LocalDate.of(1985, 12, 10));
        userController.create(user3);
        userController.addNewFriend(1, 2);
        userController.addNewFriend(1, 3);
        assertEquals(List.of(user2, user3), userController.findUserFriends(1));
    }
}