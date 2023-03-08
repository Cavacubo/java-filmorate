package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserStorage {

    User create(User user);
    void delete(User user);
    User update(User user);
    Optional<User> findUserById(int id);
    List<User> findAll();
    List<User> getUsers(Set<Integer> friends);
}
