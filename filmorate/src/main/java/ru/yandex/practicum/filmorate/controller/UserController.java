package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Current number of users: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validate(user);
        user.setId(currentId++);
        users.put(user.getId(), user);
        log.debug("User: {} added", user.getName());
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
           throw new ValidationException("id does not exist: " + user.getId());
        }
        validate(user);
        users.put(user.getId(), user);
        log.debug("User: {} updated", user.getName());
        return user;
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("email: " + user.getEmail() + " is incorrect");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("login: " + user.getLogin() + " is incorrect");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("birthday:" + user.getBirthday() + " should not be in the future" );
        }
    }
}
