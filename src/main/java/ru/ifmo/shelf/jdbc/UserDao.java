package ru.ifmo.shelf.jdbc;

import ru.ifmo.shelf.model.User;

import java.sql.*;
import java.util.Locale;

/**
 * Created by nikge on 14.12.2016.
 */
public class UserDao {

    private static Connection connection;

    static {
        Locale.setDefault(Locale.US);
        String dbURL = "jdbc:oracle:thin:@students.dce.ifmo.ru:1521:XE";
        try {
            connection = DriverManager.getConnection(dbURL, "s191976", "cjj221");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(User user) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO USERS (NAME, PASSWORD) VALUES ('" + user.getName() + "', '" + user.getPassword() + "')");
    }

    public void updateName(User user) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("UPDATE USERS SET NAME = '" + user.getName() + "' WHERE ID = " + user.getId());
    }

    public void updatePassword(User user) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("UPDATE USERS SET PASSWORD = '" + user.getPassword() + "', WHERE ID = " + user.getId());
    }

    public void delete(User user) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM USERS WHERE ID = " + user.getId());
    }

    public int connect(User user) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT ID FROM USERS WHERE NAME = '" + user.getName() + "' AND PASSWORD = '" + user.getPassword() + "'");
        resultSet.next();
        if (resultSet.isClosed()) {
            return 0;
        } else {
            return resultSet.getInt("ID");
        }
    }
}