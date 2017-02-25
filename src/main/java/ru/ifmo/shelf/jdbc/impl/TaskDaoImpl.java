package ru.ifmo.shelf.jdbc.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.ifmo.shelf.jdbc.TaskDao;
import ru.ifmo.shelf.model.DatedTasksGroup;
import ru.ifmo.shelf.model.Task;
import ru.ifmo.shelf.model.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;

/**
 * Created by nikge on 18.12.2016.
 */
@Repository
public class TaskDaoImpl implements TaskDao {

    private static SimpleDateFormat dateFormatDB = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat dateFormatTasksGroup = new SimpleDateFormat("MMMM, d");
    private static SimpleDateFormat dateFormatTask = new SimpleDateFormat("D MMM");

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public TaskDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void insert(Task task) throws SQLException {
        jdbcTemplate.update("INSERT INTO TASKS (USER_ID, NAME, DESCRIPTION, PRIORITY, END_DATE, COMPLETED, CATEGORY) VALUES (" + task.getUser() + ", '" + task.getName() + "', '" + task.getDescription() + "', '" + task.getPriority() + "', TO_DATE('" + task.getEndDate() + "', 'yyyy-MM-DD'), 0, " + task.getCategory() + ")");
    }

    @Override
    public void changePriority(int id, int priority) throws SQLException {
        jdbcTemplate.update("UPDATE TASKS SET PRIORITY = '" + priority + "' WHERE ID = " + id);
    }

    @Override
    public void changeName(int id, String name) throws SQLException {
        jdbcTemplate.update("UPDATE TASKS SET NAME = '" + name + "' WHERE ID = " + id);
    }

    @Override
    public void changeDate(int id, String date) throws SQLException {
        jdbcTemplate.update("UPDATE TASKS SET END_DATE = TO_DATE('" + date + "', 'yyyy-MM-DD') WHERE ID = " + id);
    }

    @Override
    public void changeDescription(int id, String description) throws SQLException {
        jdbcTemplate.update("UPDATE TASKS SET DESCRIPTION = '" + description + "' WHERE ID = " + id);
    }

    @Override
    public void changeCategory(int id, String category) throws SQLException {
        jdbcTemplate.update("UPDATE TASKS SET CATEGORY = '" + category + "' WHERE ID = " + id);
    }

    @Override
    public void complete(int id) throws SQLException {
        jdbcTemplate.update("UPDATE TASKS SET COMPLETED = 1 WHERE ID = " + id);
    }

    @Override
    public void delete(int id) throws SQLException {
        jdbcTemplate.update("DELETE FROM TASKS WHERE ID = " + id);
    }

    @Override
    public String getCategoryName(int id) {
        return jdbcTemplate.queryForObject("SELECT NAME FROM CATEGORIES WHERE ID = ?", new Object[]{id}, String.class);
    }

    private LinkedHashSet<DatedTasksGroup> resultSetToDatedTasksSet(SqlRowSet resultSet) throws SQLException, ParseException {
        LinkedHashSet<DatedTasksGroup> datedSet = new LinkedHashSet<DatedTasksGroup>();

        LinkedHashSet<Task> currentDateSet = new LinkedHashSet<Task>();
        Date currentDate = new Date(0);
        Boolean currentDateCompleted = false;
        while (resultSet.next()) {
            int id = resultSet.getInt("ID");
            int userId = resultSet.getInt("USER_ID");
            int priority = resultSet.getInt("PRIORITY");
            String name = resultSet.getString("NAME");
            String description = resultSet.getString("DESCRIPTION");
            int category = resultSet.getInt("CATEGORY");
            String categoryName = getCategoryName(category);
            Date endDate = dateFormatDB.parse(resultSet.getString("END_DATE"));
            String taskDate = dateFormatTask.format(endDate);
            boolean completed = resultSet.getBoolean("COMPLETED");
            Task task = new Task(id, userId, name, taskDate, priority, description, currentDateCompleted, category, categoryName);

            if (currentDate.equals(new Date(0))) {
                currentDate = endDate;
            }

            if (!endDate.equals(currentDate)) {
                DatedTasksGroup tasksGroup = new DatedTasksGroup();
                tasksGroup.setTime(dateFormatTasksGroup.format(currentDate));
                tasksGroup.setTimeDB(dateFormatDB.format(endDate));
                System.out.println(dateFormatDB.format(endDate));
                tasksGroup.setTasks(currentDateSet);
                if (!currentDateCompleted) {
                    tasksGroup.setOverdue(isOverdue(currentDate));
                } else {
                    tasksGroup.setOverdue(false);
                    tasksGroup.setCompleted(true);
                }
                datedSet.add(tasksGroup);
                currentDate = endDate
                ;
                currentDateCompleted = completed;
                currentDateSet = new LinkedHashSet<Task>();
            }
            currentDateSet.add(task);
            currentDateCompleted = completed;
        }

        if (currentDateSet.size() > 0) {
            DatedTasksGroup tasksGroup = new DatedTasksGroup();
            tasksGroup.setTime(dateFormatTasksGroup.format(currentDate));
            tasksGroup.setTimeDB(dateFormatDB.format(currentDate));
            System.out.println(dateFormatDB.format(currentDate));
            tasksGroup.setTasks(currentDateSet);
            if (!currentDateCompleted) {
                tasksGroup.setOverdue(isOverdue(currentDate));
            } else {
                tasksGroup.setOverdue(false);
                tasksGroup.setCompleted(true);
            }
            datedSet.add(tasksGroup);
        }

        return datedSet;
    }

    private boolean isOverdue(Date date) {
        return date.before(new Date(System.currentTimeMillis() - 86400000));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getUnfinishedTasks(User user) throws SQLException, ParseException {
        Date currentDate = new Date(System.currentTimeMillis());
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND COMPLETED = 0 AND END_DATE < TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') ORDER BY END_DATE, PRIORITY DESC";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getTasksForToday(User user) throws SQLException, ParseException {
        Date currentDate = new Date(System.currentTimeMillis());
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND COMPLETED = 0 AND END_DATE = TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') ORDER BY END_DATE, PRIORITY DESC";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getTasksForTomorrow(User user) throws SQLException, ParseException {
        Date tomorrowDate = new Date(System.currentTimeMillis() + 86400000);
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND COMPLETED = 0 AND END_DATE = TO_DATE('" + dateFormatDB.format(tomorrowDate) + "', 'YYYY-MM-DD') ORDER BY END_DATE, PRIORITY DESC";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getTasksForWeek(User user) throws SQLException, ParseException {
        Date currentDate = new Date(System.currentTimeMillis());
        Date dateAfterWeek = new Date(System.currentTimeMillis() + 518400000);
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND COMPLETED = 0 AND END_DATE >= TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') AND END_DATE <= TO_DATE('" + dateFormatDB.format(dateAfterWeek) + "', 'YYYY-MM-DD') ORDER BY END_DATE, PRIORITY DESC";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getCompletedTasks(User user) throws SQLException, ParseException {
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND COMPLETED = 1 ORDER BY END_DATE DESC, PRIORITY DESC";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getInitialTasks(User user) throws SQLException, ParseException {
        Date currentDate = new Date(System.currentTimeMillis());
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND COMPLETED = 0 AND END_DATE <= TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') ORDER BY END_DATE, PRIORITY DESC";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getTasksByCategory(User user, String category) throws SQLException, ParseException {
        Date currentDate = new Date(System.currentTimeMillis());
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND CATEGORY = '" + category + "' AND COMPLETED = 0 AND END_DATE >= TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') ORDER BY END_DATE, PRIORITY DESC";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }
}