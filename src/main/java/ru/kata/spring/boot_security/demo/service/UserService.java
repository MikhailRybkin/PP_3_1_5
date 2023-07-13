package ru.kata.spring.boot_security.demo.service;



import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void add (User user);

    void update (User user);

    void delete (Long id);

    List<User> getAll ();

    User show (Long id);

    User findByUsername (String email);
}
