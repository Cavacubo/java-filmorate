package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService = new UserService(new InMemoryUserStorage());

    @Test
    void findAllShouldReturnEmptyMapIfNothingAdded() {
        assertEquals(0, userService.findAllUsers().size());
        assertTrue(userService.findAllUsers().isEmpty());
    }

    @Test
    void findUserByIdHappyPath() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        userService.createUser(user);
        assertEquals(1, userService.findUserById(1).getId());
        assertEquals("Jane", userService.findUserById(1).getName());
    }

    @Test
    void findUserByIdShouldThrowExceptionIfUserDoesNotExist() {
        assertThrows(NotFoundException.class, () -> userService.findUserById(5555));
    }

    @Test
    void createUserHappyPath() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        assertEquals(user, userService.createUser(user));
        assertEquals(1, userService.findAllUsers().size());
    }

    @Test
    void createShouldUseLoginIfUserNameIsNull() {
        User user = new User(1, "email@gmail.com", "user", null, LocalDate.of(1990, 12, 10));
        userService.createUser(user);
        assertEquals("user", user.getName());
    }

    @Test
    void createShouldUseLoginIfUserNameIsEmpty() {
        User user = new User(1, "email@gmail.com", "user", "", LocalDate.of(1990, 12, 10));
        userService.createUser(user);
        assertEquals("user", user.getName());
    }

    @Test
    void createShouldThrowExceptionIfUserEmailIsNull() {
        User user = new User(1, null, "user", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userService.createUser(user));
    }

    @Test
    void createShouldThrowExceptionIfUserEmailDoesNotContainAt() {
        User user = new User(1, "email.gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userService.createUser(user));
    }

    @Test
    void createShouldThrowExceptionIfUserLoginIsNull() {
        User user = new User(1, "email@gmail.com", null, "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userService.createUser(user));
    }

    @Test
    void createShouldThrowExceptionIfUserLoginIsEmpty() {
        User user = new User(1, "email@gmail.com", "", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userService.createUser(user));
    }

    @Test
    void createShouldThrowExceptionIfUserLoginContainsSpacesAtTheBeginning() {
        User user = new User(1, "email@gmail.com", "  user", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userService.createUser(user));
    }

    @Test
    void createShouldThrowExceptionIfUserLoginContainsSpacesAtTheEnd() {
        User user = new User(1, "email@gmail.com", "user ", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userService.createUser(user));
    }

    @Test
    void createShouldThrowExceptionIfUserLoginContainsSpacesInTheMiddle() {
        User user = new User(1, "email@gmail.com", "us  er", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userService.createUser(user));
    }

    @Test
    void createShouldThrowExceptionIfUserLoginContainsOnlySpaces() {
        User user = new User(1, "email@gmail.com", "   ", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(BadRequestException.class, () -> userService.createUser(user));
    }

    @Test
    void createShouldThrowExceptionIfUserBirthdayIsInTheFuture() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(2023, 10, 15));
        assertThrows(BadRequestException.class, () -> userService.createUser(user));
    }

    @Test
    void updateShouldThrowExceptionIfFilmIdIsNotExist() {
        User user = new User(55, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        assertThrows(NotFoundException.class, () -> userService.updateUser(user));
    }

    @Test
    void deleteUser() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        userService.createUser(user);
        userService.deleteUser(user);
        assertEquals(0, userService.findAllUsers().size());
    }

    @Test
    void addNewFriend() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        userService.createUser(user);
        User user2 = new User(2, "email2@gmail.com", "user2", "Kate", LocalDate.of(1980, 12, 10));
        userService.createUser(user2);
        userService.addNewFriend(1, 2);
        assertEquals(Set.of(2), user.getFriends());
    }

    @Test
    void deleteFriend() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        userService.createUser(user);
        User user2 = new User(2, "email2@gmail.com", "user2", "Kate", LocalDate.of(1980, 12, 10));
        userService.createUser(user2);
        userService.addNewFriend(1, 2);
        userService.deleteFriend(1, 2);
        assertEquals(Set.of(), user.getFriends());
    }

    @Test
    void getCommonFriendsList() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        userService.createUser(user);
        User user2 = new User(2, "email2@gmail.com", "user2", "Kate", LocalDate.of(1980, 12, 10));
        userService.createUser(user2);
        User user3 = new User(3, "email3@gmail.com", "user3", "Lukas", LocalDate.of(1985, 12, 10));
        userService.createUser(user3);
        userService.addNewFriend(1, 3);
        userService.addNewFriend(2, 3);
        assertEquals(List.of(user3), userService.getCommonFriendsList(1, 2));
    }

    @Test
    void getUserFriendsList() {
        User user = new User(1, "email@gmail.com", "user", "Jane", LocalDate.of(1990, 12, 10));
        userService.createUser(user);
        User user2 = new User(2, "email2@gmail.com", "user2", "Kate", LocalDate.of(1980, 12, 10));
        userService.createUser(user2);
        User user3 = new User(3, "email3@gmail.com", "user3", "Lukas", LocalDate.of(1985, 12, 10));
        userService.createUser(user3);
        userService.addNewFriend(1, 2);
        userService.addNewFriend(1, 3);
        assertEquals(List.of(user2, user3), userService.getUserFriendsList(1));
    }
}