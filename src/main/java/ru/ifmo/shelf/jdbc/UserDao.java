package ru.ifmo.shelf.jdbc;

import ru.ifmo.shelf.model.User;

import java.sql.SQLException;

public interface UserDao {

    void insert(User user) throws SQLException;

    void changeName(User user) throws SQLException;

    void changePassword(User user) throws SQLException;

    void delete(User user) throws SQLException;

    int connect(User user) throws SQLException;

    boolean nameAlreadyTaken(User user) throws SQLException;

}
