package ru.ifmo.shelf.model;

import javax.persistence.*;
/**
 * Created by nikge on 15.12.2016.
 */
@Entity
@Table(name = "TASKS")
public class Task {

    public Task() {}

    public Task(int id, int user, String name, String endDate, int priority, String description, Boolean completed, int category, String categoryName) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.endDate = endDate;
        this.priority = priority;
        this.description = description;
        this.completed = completed;
        this.category = category;
        this.categoryName = categoryName;
    }

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    @Column(name = "USER_ID")
    private int user;

    @Column(name = "NAME")
    private String name;

    @Column(name = "END_DATE")
    private String endDate;

    @Column(name = "PRIORITY")
    private int priority;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "COMPLETED")
    private Boolean completed;

    @Column(name = "CATEGORY")
    private int category;

    private String categoryName;

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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String time) {
        this.endDate = time;
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

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
