package ru.ifmo.shelf.model;

import javax.persistence.*;

/**
 * Created by nikge on 09.01.2017.
 */
@Entity
@Table(name = "CATEGORY")
public class Category {

    public Category(String name, int userId) {
        this.name = name;
        this.userId = userId;
    }

    public Category(int id, String name, int userId) {
        this.id = id;
        this.name = name;
        this.userId = userId;
    }

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "USER_ID")
    private int userId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
