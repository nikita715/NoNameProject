package ru.ifmo.shelf.jdbc;

import ru.ifmo.shelf.model.User;
import sun.rmi.runtime.Log;

import java.sql.*;

/**
 * Created by nikge on 14.12.2016.
 */
public class UserDao {
    private static Connection connection;

    public UserDao() throws SQLException {
        String dbURL = "jdbc:sqlite:src/main/resources/database.s3db";
        connection = DriverManager.getConnection(dbURL);
        if (connection != null) {
            System.out.println("DB OK");
        }
    }

    public void insert(User user) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT into USERS (username, password) VALUES ('" + user.getUsername() + "', '" + user.getPassword() + "')");
    }

    public Integer getUsersQuantity() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM USERS");

        int count = 0;
        while (resultSet.next()) {
            count++;
            System.out.println(count);
        }

        return count;
    }
}