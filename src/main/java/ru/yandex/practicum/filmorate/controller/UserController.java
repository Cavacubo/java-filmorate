package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll() {
        log.info("Current number of users: {}", userService.findAllUsers().size());
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable("id") int userId) {
        return userService.findUserById(userId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findUserFriends(@PathVariable("id") int userId) {
        log.info("Current number of user friends: {}", userService.getUserFriendsList(userId).size());
        return userService.getUserFriendsList(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("id") int userId, @PathVariable("otherId") int friendId) {
        log.info("Current number of user friends: {}", userService.getCommonFriendsList(userId, friendId).size());
        return userService.getCommonFriendsList(userId, friendId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.debug("User: {} added", user.getName());
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.debug("User: {} updated", user.getName());
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addNewFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        if (friendId < 0) {
            throw new IncorrectParameterException("friendId");
        }
        log.debug("Friend: {} added into friends", userService.findUserById(friendId).getName());
        userService.addNewFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
        log.debug("Friend: {} deleted from friends", userService.findUserById(friendId).getName());
        userService.deleteFriend(userId, friendId);
    }
}
