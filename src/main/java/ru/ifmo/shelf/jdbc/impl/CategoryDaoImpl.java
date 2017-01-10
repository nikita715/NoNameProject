package ru.ifmo.shelf.jdbc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.ifmo.shelf.jdbc.CategoryDao;
import ru.ifmo.shelf.model.Category;
import ru.ifmo.shelf.model.User;

import javax.sql.DataSource;
import java.util.LinkedHashSet;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CategoryDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(Category category) {
        jdbcTemplate.update("INSERT INTO CATEGORIES (NAME, USER_ID) VALUES ('" + category.getName() +  "', " + category.getUserId() + ")");
    }

    @Override
    public void changeName(int id, String name) {
        jdbcTemplate.update("UPDATE CATEGORIES SET NAME = '" + name + "' WHERE ID = " + id);
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM CATEGORIES WHERE ID = " + id);
    }

    private LinkedHashSet<Category> convertToSet(SqlRowSet sqlRowSet) {
        LinkedHashSet<Category> linkedHashSet = new LinkedHashSet<>();

        while (sqlRowSet.next()) {
            int id = sqlRowSet.getInt("ID");
            String name = sqlRowSet.getString("NAME");
            int userId = sqlRowSet.getInt("USER_ID");
            linkedHashSet.add(new Category(id, name, userId));
        }

        return linkedHashSet;
    }

    @Override
    public LinkedHashSet<Category> getCategories(User user) {
        String sql = "SELECT * FROM CATEGORIES WHERE USER_ID = " + user.getId();
        return convertToSet(jdbcTemplate.queryForRowSet(sql));
    }
}
