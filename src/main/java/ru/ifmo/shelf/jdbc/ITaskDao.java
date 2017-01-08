package ru.ifmo.shelf.jdbc;

import ru.ifmo.shelf.model.DatedTasksGroup;
import ru.ifmo.shelf.model.Task;
import ru.ifmo.shelf.model.User;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedHashSet;

/**
 * Created by nikge on 07.01.2017.
 */
public interface ITaskDao {

    void insert(Task task) throws SQLException;

    void updatePriority(int id, int priority) throws SQLException;

    void updateName(int id, String name) throws SQLException;

    void updateTime(int id, String time) throws SQLException;

    void updateDescription(int id, String description) throws SQLException;

    void complete(int id) throws SQLException;

    void delete(int id) throws SQLException;

    LinkedHashSet<DatedTasksGroup> getUnfinishedTasks(User user) throws SQLException, ParseException;

    LinkedHashSet<DatedTasksGroup> getTasksForToday(User user) throws SQLException, ParseException;

    LinkedHashSet<DatedTasksGroup> getTasksForTomorrow(User user) throws SQLException, ParseException;

    LinkedHashSet<DatedTasksGroup> getTasksForWeek(User user) throws SQLException, ParseException;

    LinkedHashSet<DatedTasksGroup> getCompletedTasks(User user) throws SQLException, ParseException;

    LinkedHashSet<DatedTasksGroup> getInitialTasks(User user) throws SQLException, ParseException;
}
