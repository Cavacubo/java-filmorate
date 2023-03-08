package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public User getUserById(Integer id) {
        return userStorage.findUserById(id)
                .orElseThrow(() -> new NotFoundException("Film with id: " + id + " is not found"));
    }

    public User createUser(User user) {
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        return userStorage.update(user);
    }

    public void deleteUser(User user) {
        userStorage.delete(user);
    }

    public void addNewFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
    }

    public List<User> getCommonFriendsList(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        Set<Integer> commonFriends = new HashSet<>(user.getFriends());
        commonFriends.retainAll(friend.getFriends());
        return userStorage.getUsers(commonFriends);
    }

    public List<User> getUserFriendsList(int userId) {
        User user = getUserById(userId);
        return userStorage.getUsers(user.getFriends());
    }

//    private List<User> getUsers(Set<Integer> friends) {
//        return friends.stream()
//                .map(userStorage::findUserById)
//                .collect(Collectors.toList());
//    }
}
