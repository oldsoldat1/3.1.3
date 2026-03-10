package ru.kata.spring.boot_security.demo.dao;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserDao {

    void addUser(User user);

    void updateUser(User user);

    void removeUser(int id);

    User getUser(int id);

    List<User> getAllUsers();

    User findByUsername(String username);

}

