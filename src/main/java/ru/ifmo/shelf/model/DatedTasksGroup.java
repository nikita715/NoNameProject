package ru.ifmo.shelf.model;

import java.util.LinkedHashSet;

/**
 * Created by nikge on 05.01.2017.
 */
public class DatedTasksGroup {

    private String time;

    private String timeDB;

    private LinkedHashSet<Task> tasks;

    private Boolean overdue;

    private Boolean completed;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LinkedHashSet<Task> getTasks() {
        return tasks;
    }

    public void setTasks(LinkedHashSet<Task> tasks) {
        this.tasks = tasks;
    }

    public Boolean getOverdue() {
        return overdue;
    }

    public void setOverdue(Boolean overdue) {
        this.overdue = overdue;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public String getTimeDB() {
        return timeDB;
    }

    public void setTimeDB(String timeDB) {
        this.timeDB = timeDB;
    }
}
