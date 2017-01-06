package ru.ifmo.shelf.jdbc;

import ru.ifmo.shelf.model.DatedTasksGroup;
import ru.ifmo.shelf.model.Task;
import ru.ifmo.shelf.model.User;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;

/**
 * Created by nikge on 18.12.2016.
 */
public class TaskDao {

    private static Connection connection;
    private static Date currentDate;
    private static SimpleDateFormat dateFormatDB;
    private static SimpleDateFormat dateFormatTasksGroup;
    private static SimpleDateFormat dateFormatTask;

    static {
        Locale.setDefault(Locale.US);
        String dbURL = "jdbc:oracle:thin:@students.dce.ifmo.ru:1521:XE";
        dateFormatDB = new SimpleDateFormat("yyyy-MM-DD");
        dateFormatTasksGroup = new SimpleDateFormat("MMMM, D");
        dateFormatTask = new SimpleDateFormat("D MMM");
        try {
            connection = DriverManager.getConnection(dbURL, "s191976", "cjj221");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insert(Task task) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("INSERT INTO TASKS (USER_ID, NAME, DESCRIPTION, PRIORITY, TIME) VALUES (" + task.getUser() + ", '" + task.getName() + "', '" + task.getDescription() + "', '" + task.getPriority() + "', TO_DATE('" + task.getTime() + "', 'yyyy-MM-DD'))");
    }

    public void updatePriority(Task task) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("UPDATE TASKS SET PRIORITY = '" + task.getPriority() + "' WHERE ID = " + task.getId());
    }

    public void updateName(Task task) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("UPDATE TASKS SET NAME = '" + task.getName() + "' WHERE ID = " + task.getId());
    }

    public void updateTime(Task task) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("UPDATE TASKS SET TIME = '" + task.getTime() + "' WHERE ID = " + task.getId());
    }

    public void updateDescription(Task task) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("UPDATE TASKS SET DESCRIPTION = '" + task.getDescription() + "' WHERE ID = " + task.getId());
    }

    public void delete(Task task) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DELETE FROM TASKS WHERE ID = " + task.getId());
    }

    private LinkedHashSet<DatedTasksGroup> resultSetToDatedTasksSet(ResultSet resultSet) throws SQLException, ParseException {
        LinkedHashSet<DatedTasksGroup> datedSet = new LinkedHashSet<DatedTasksGroup>();

        LinkedHashSet<Task> currentDateSet = new LinkedHashSet<Task>();
        Date currentDate = new Date(0);
        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            int userId = resultSet.getInt("USER_ID");
            int priority = resultSet.getInt("PRIORITY");
            String name = resultSet.getString("NAME");
            String description = resultSet.getString("DESCRIPTION");
            Date date = dateFormatDB.parse(resultSet.getString("TIME"));
            String taskDate = dateFormatTask.format(date);
            Task task = new Task(id, userId, name, taskDate, priority, description);

            if (currentDate.equals(new Date(0))) {
                currentDate = date;
            }

            if (!date.equals(currentDate)) {
                DatedTasksGroup tasksGroup = new DatedTasksGroup();
                tasksGroup.setTime(dateFormatTasksGroup.format(currentDate));
                tasksGroup.setTasks(currentDateSet);
                tasksGroup.setOverdue(isOverdue(currentDate));
                datedSet.add(tasksGroup);
                currentDate = date;
                currentDateSet = new LinkedHashSet<Task>();
            }
            currentDateSet.add(task);
        }

        if (currentDateSet.size() > 0) {
            DatedTasksGroup tasksGroup = new DatedTasksGroup();
            tasksGroup.setTime(dateFormatTasksGroup.format(currentDate));
            tasksGroup.setTasks(currentDateSet);
            tasksGroup.setOverdue(isOverdue(currentDate));
            datedSet.add(tasksGroup);
        }

        return datedSet;
    }

    private Boolean isOverdue(Date date) {
        return date.before(new Date(System.currentTimeMillis() - 86400000 ));
    }

    public LinkedHashSet<DatedTasksGroup> getOldTasks(User user) throws SQLException, ParseException {
        Statement statement = connection.createStatement();
        currentDate = new Date(System.currentTimeMillis());
        ResultSet resultSet = statement.executeQuery("SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND TIME < TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') ORDER BY TIME, PRIORITY DESC");
        return resultSetToDatedTasksSet(resultSet);
    }

    public LinkedHashSet<DatedTasksGroup> getTodayTasks(User user) throws SQLException, ParseException {
        Statement statement = connection.createStatement();
        currentDate = new Date(System.currentTimeMillis());
        ResultSet resultSet = statement.executeQuery("SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND TIME = TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') ORDER BY TIME, PRIORITY DESC");
        return resultSetToDatedTasksSet(resultSet);
    }

    public LinkedHashSet<DatedTasksGroup> getTomorrowTasks(User user) throws SQLException, ParseException {
        Statement statement = connection.createStatement();
        currentDate = new Date(System.currentTimeMillis() + 86400000);
        ResultSet resultSet = statement.executeQuery("SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND TIME = TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') ORDER BY TIME, PRIORITY DESC");
        return resultSetToDatedTasksSet(resultSet);
    }

    public LinkedHashSet<DatedTasksGroup> getWeekTasks(User user) throws SQLException, ParseException {
        Statement statement = connection.createStatement();
        currentDate = new Date(System.currentTimeMillis());
        Date dateAfterWeek = new Date(System.currentTimeMillis() + 518400000);
        ResultSet resultSet = statement.executeQuery("SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND TIME >= TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') AND TIME <= TO_DATE('" + dateFormatDB.format(dateAfterWeek) + "', 'YYYY-MM-DD') ORDER BY TIME, PRIORITY DESC");
        return resultSetToDatedTasksSet(resultSet);
    }
}