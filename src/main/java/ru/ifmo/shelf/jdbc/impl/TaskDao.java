package ru.ifmo.shelf.jdbc.impl;

import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.ifmo.shelf.jdbc.ITaskDao;
import ru.ifmo.shelf.model.DatedTasksGroup;
import ru.ifmo.shelf.model.Task;
import ru.ifmo.shelf.model.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;

/**
 * Created by nikge on 18.12.2016.
 */
@Repository
public class TaskDao implements ITaskDao {

    private static SimpleDateFormat dateFormatDB = new SimpleDateFormat("yyyy-MM-DD");
    private static SimpleDateFormat dateFormatTasksGroup = new SimpleDateFormat("MMMM, D");
    private static SimpleDateFormat dateFormatTask = new SimpleDateFormat("D MMM");

    private JdbcTemplate jdbcTemplate;

    public TaskDao() {
        Locale.setDefault(Locale.US);
        GenericXmlApplicationContext ctx = new GenericXmlApplicationContext();
        ctx.load("application-context.xml");
        ctx.refresh();
        this.jdbcTemplate = new JdbcTemplate(ctx.getBean ( "springDataSource", DataSource.class));
    }

    @Override
    public void insert(Task task) throws SQLException {
        jdbcTemplate.update("INSERT INTO TASKS (USER_ID, NAME, DESCRIPTION, PRIORITY, TIME) VALUES (" + task.getUser() + ", '" + task.getName() + "', '" + task.getDescription() + "', '" + task.getPriority() + "', TO_DATE('" + task.getTime() + "', 'yyyy-MM-DD'))");
    }

    @Override
    public void updatePriority(int id, int priority) throws SQLException {
        jdbcTemplate.update("UPDATE TASKS SET PRIORITY = '" + priority + "' WHERE ID = " + id);
    }

    @Override
    public void updateName(int id, String name) throws SQLException {
        jdbcTemplate.update("UPDATE TASKS SET NAME = '" + name + "' WHERE ID = " + id);
    }

    @Override
    public void updateTime(int id, String time) throws SQLException {
        jdbcTemplate.update("UPDATE TASKS SET TIME = '" + time + "' WHERE ID = " + id);
    }

    @Override
    public void updateDescription(int id, String description) throws SQLException {
        jdbcTemplate.update("UPDATE TASKS SET DESCRIPTION = '" + description + "' WHERE ID = " + id);
    }

    @Override
    public void complete(int id) throws SQLException {
        jdbcTemplate.update("UPDATE TASKS SET COMPLETED = 1 WHERE ID = " + id);
    }

    @Override
    public void delete(int id) throws SQLException {
        jdbcTemplate.update("DELETE FROM TASKS WHERE ID = " + id);
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
            Date date = dateFormatDB.parse(resultSet.getString("TIME"));
            String taskDate = dateFormatTask.format(date);
            Boolean completed = resultSet.getBoolean("COMPLETED");
            Task task = new Task(id, userId, name, taskDate, priority, description, currentDateCompleted);

            if (currentDate.equals(new Date(0))) {
                currentDate = date;
            }

            if (!date.equals(currentDate)) {
                DatedTasksGroup tasksGroup = new DatedTasksGroup();
                tasksGroup.setTime(dateFormatTasksGroup.format(currentDate));
                tasksGroup.setTasks(currentDateSet);
                if (!currentDateCompleted) {
                    tasksGroup.setOverdue(isOverdue(currentDate));
                } else {
                    tasksGroup.setOverdue(false);
                }
                datedSet.add(tasksGroup);
                currentDate = date;
                currentDateSet = new LinkedHashSet<Task>();
            }
            currentDateSet.add(task);
            currentDateCompleted = completed;
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
        return date.before(new Date(System.currentTimeMillis() - 86400000));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getUnfinishedTasks(User user) throws SQLException, ParseException {
        Date currentDate = new Date(System.currentTimeMillis());
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND COMPLETED = 0 AND TIME < TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') ORDER BY TIME, PRIORITY DESC";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getTasksForToday(User user) throws SQLException, ParseException {
        Date currentDate = new Date(System.currentTimeMillis());
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND COMPLETED = 0 AND TIME = TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') ORDER BY TIME, PRIORITY DESC";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getTasksForTomorrow(User user) throws SQLException, ParseException {
        Date tomorrowDate = new Date(System.currentTimeMillis() + 86400000);
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND COMPLETED = 0 AND TIME = TO_DATE('" + dateFormatDB.format(tomorrowDate) + "', 'YYYY-MM-DD') ORDER BY TIME, PRIORITY DESC";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getTasksForWeek(User user) throws SQLException, ParseException {
        Date currentDate = new Date(System.currentTimeMillis());
        Date dateAfterWeek = new Date(System.currentTimeMillis() + 518400000);
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND COMPLETED = 0 AND TIME >= TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') AND TIME <= TO_DATE('" + dateFormatDB.format(dateAfterWeek) + "', 'YYYY-MM-DD')";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getCompletedTasks(User user) throws SQLException, ParseException {
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND COMPLETED = 1 ORDER BY TIME DESC, PRIORITY DESC";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }

    @Override
    public LinkedHashSet<DatedTasksGroup> getInitialTasks(User user) throws SQLException, ParseException {
        Date currentDate = new Date(System.currentTimeMillis());
        String sql = "SELECT * FROM TASKS WHERE USER_ID = " + user.getId() + " AND COMPLETED = 0 AND TIME < TO_DATE('" + dateFormatDB.format(currentDate) + "', 'YYYY-MM-DD') ORDER BY TIME, PRIORITY DESC";
        return resultSetToDatedTasksSet(jdbcTemplate.queryForRowSet(sql));
    }
}