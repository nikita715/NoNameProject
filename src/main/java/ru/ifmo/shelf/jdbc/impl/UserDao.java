package ru.ifmo.shelf.jdbc.impl;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ifmo.shelf.jdbc.IUserDao;
import ru.ifmo.shelf.model.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Created by nikge on 14.12.2016.
 */
@Repository
@EnableAutoConfiguration
public class UserDao implements IUserDao {

    private JdbcTemplate jdbcTemplate;

    public UserDao() {
        Locale.setDefault(Locale.US);
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load("application-context.xml");
        ctx.refresh();
        this.jdbcTemplate = new JdbcTemplate(ctx.getBean ( "springDataSource", DataSource.class));
    }

    @Override
    public void insert(User user) throws SQLException {
        jdbcTemplate.update("INSERT INTO USERS (NAME, PASSWORD) VALUES ('" + user.getName() + "', '" + user.getPassword() + "')");
    }

    @Override
    public void updateName(User user) throws SQLException {
        jdbcTemplate.update("UPDATE USERS SET NAME = '" + user.getName() + "' WHERE ID = " + user.getId());
    }

    @Override
    public void updatePassword(User user) throws SQLException {
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
    public Boolean nameAlreadyTaken(User user) throws SQLException {
        int count = jdbcTemplate.queryForObject("SELECT COUNT(ID) FROM USERS WHERE NAME = '" + user.getName() + "'", new Object[]{}, Integer.class);
        return count > 0;
    }
}