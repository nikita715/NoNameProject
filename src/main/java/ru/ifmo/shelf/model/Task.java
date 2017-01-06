package ru.ifmo.shelf.model;

import javax.persistence.*;
/**
 * Created by nikge on 15.12.2016.
 */
@Entity
@Table(name = "TASKS")
public class Task {

    public Task() {}

    public Task(int id, int user, String name, String time, int priority, String description) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.time = time;
        this.priority = priority;
        this.description = description;
    }

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    @Column(name = "USER_ID")
    private int user;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TIME")
    private String time;

    @Column(name = "PRIORITY")
    private int priority;

    @Column(name = "DESCRIPTION")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
