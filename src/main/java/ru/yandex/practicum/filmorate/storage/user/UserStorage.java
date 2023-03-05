package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User create(User user);
    void delete(User user);
    User update(User user);
    User getUserById(int id);
    List<User> findAll();
}
