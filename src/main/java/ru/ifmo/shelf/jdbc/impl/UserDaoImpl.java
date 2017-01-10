package ru.ifmo.shelf.jdbc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.ifmo.shelf.jdbc.UserDao;
import ru.ifmo.shelf.model.User;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by nikge on 14.12.2016.
 */
@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(User user) throws SQLException {
        jdbcTemplate.update("INSERT INTO USERS (NAME, PASSWORD) VALUES ('" + user.getName() + "', '" + user.getPassword() + "')");
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT ID FROM USERS WHERE NAME = '" + user.getName() + "'");
        rowSet.next();
        int userId = rowSet.getInt("ID");
        jdbcTemplate.update("INSERT INTO CATEGORIES (NAME, USER_ID) VALUES ('Personal', " + userId + ")");
    }

    @Override
    public void changeName(User user) throws SQLException {
        jdbcTemplate.update("UPDATE USERS SET NAME = '" + user.getName() + "' WHERE ID = " + user.getId());
    }

    @Override
    public void changePassword(User user) throws SQLException {
        jdbcTemplate.update("UPDATE USERS SET PASSWORD = '" + user.getPassword() + "', WHERE ID = " + user.getId());
    }

    @Override
    public void delete(User user) throws SQLException {
        jdbcTemplate.update("DELETE FROM USERS WHERE ID = " + user.getId());
    }

    @Override
    public int connect(User user) throws SQLException {
        try {
            return jdbcTemplate.queryForObject("SELECT ID FROM USERS WHERE NAME = ? AND PASSWORD = ?", new Object[]{user.getName(), user.getPassword()}, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean nameAlreadyTaken(User user) throws SQLException {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(ID) FROM USERS WHERE NAME = '" + user.getName() + "'", new Object[]{}, Integer.class);
        return count > 0;
    }
}