package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAllUsers() {
        return userStorage.findAll();
    }

    public User findUserById(Integer id) {
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        validate(user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        validate(user);
        return userStorage.update(user);
    }

    public void deleteUser(User user) {
        userStorage.delete(user);
    }

    public void addNewFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
    }

    public List<User> getCommonFriendsList(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        Set<Integer> commonFriends = new HashSet<>(user.getFriends());
        commonFriends.retainAll(friend.getFriends());
        return getUsers(commonFriends);
    }

    public List<User> getUserFriendsList(int userId) {
        User user = userStorage.getUserById(userId);
        return getUsers(user.getFriends());
    }

    private List<User> getUsers(Set<Integer> friends) {
        return friends.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
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
