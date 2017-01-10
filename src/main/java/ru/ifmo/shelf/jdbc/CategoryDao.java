package ru.ifmo.shelf.jdbc;

import ru.ifmo.shelf.model.Category;
import ru.ifmo.shelf.model.User;

import java.util.LinkedHashSet;

public interface CategoryDao {

    void insert(Category category);

    void changeName(int id, String name);

    void delete(int id);

    LinkedHashSet<Category> getCategories(User user);
}
