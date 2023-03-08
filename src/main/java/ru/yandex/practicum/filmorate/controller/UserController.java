package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
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
        List<User> users = userService.findAllUsers();
        log.info("Current number of users: {}", users.size());
        return users;
    }

    @GetMapping("/{id}")
    public User findUser(@PathVariable("id") int userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{id}/friends")
    public List<User> findUserFriends(@PathVariable("id") int userId) {
        List<User> userFriendsList = userService.getUserFriendsList(userId);
        log.info("Current number of user friends: {}", userFriendsList.size());
        return userFriendsList;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("id") int userId, @PathVariable("otherId") int friendId) {
        List<User> commonFriendsList = userService.getCommonFriendsList(userId, friendId);
        log.info("Current number of user friends: {}", commonFriendsList.size());
        return commonFriendsList;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validate(user);
        log.debug("User: {} added", user.getName());
        return userService.createUser(user);
    }

    @PutMapping
    public User update(@RequestBody User user) {
        validate(user);
        log.debug("User: {} updated", user.getName());
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addNewFriend(@PathVariable("id") int userId, @PathVariable("friendId") int friendId) {
        if (friendId < 0) {
            throw new NotFoundException("Friend Id: " + friendId + " should be a positive number");
        }
        log.debug("Friend: {} added into friends", userService.getUserById(friendId).getName());
        userService.addNewFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer userId, @PathVariable("friendId") Integer friendId) {
        if (friendId < 0) {
            throw new NotFoundException("Friend Id: " + friendId + " should be a positive number");
        }
        log.debug("Friend: {} deleted from friends", userService.getUserById(friendId).getName());
        userService.deleteFriend(userId, friendId);
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new BadRequestException("email: " + user.getEmail() + " is incorrect");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new BadRequestException("login: " + user.getLogin() + " is incorrect");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new BadRequestException("birthday:" + user.getBirthday() + " should not be in the future" );
        }
    }
}
