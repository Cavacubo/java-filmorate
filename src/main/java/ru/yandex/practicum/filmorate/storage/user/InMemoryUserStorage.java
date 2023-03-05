package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int currentId = 1;

    @Override
    public List<User> findAll() {
        return List.copyOf(users.values());
    }

    @Override
    public User create(User user) {
        user.setId(currentId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(User user) {
        users.remove(user.getId());
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("id does not exist: " + user.getId());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(int id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with id: " + id + " is not found");
        }
        return users.get(id);
    }

}
