package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserService userService = new UserService(new InMemoryUserStorage());

    @Test
    void findAllUsers() {
    }

    @Test
    void findUserById() {
    }

    @Test
    void createUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void deleteUser() {
    }

    @Test
    void addNewFriend() {
    }

    @Test
    void deleteFriend() {
    }

    @Test
    void getCommonFriendsList() {
    }

    @Test
    void getUserFriendsList() {
    }
}