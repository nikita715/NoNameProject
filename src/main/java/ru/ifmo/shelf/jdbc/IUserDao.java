package ru.ifmo.shelf.jdbc;

import ru.ifmo.shelf.model.User;

import java.sql.SQLException;

public interface IUserDao {

    void insert(User user) throws SQLException;

    void updateName(User user) throws SQLException;

    void updatePassword(User user) throws SQLException;

    void delete(User user) throws SQLException;

    int connect(User user) throws SQLException;

    Boolean nameAlreadyTaken(User user) throws SQLException;

}
