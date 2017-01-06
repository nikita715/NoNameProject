package ru.ifmo.shelf.model;

import javax.persistence.*;
/**
 * Created by nikge on 08.11.2016.
 */
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "PASSWORD")
    private String password;

    private String passwordСheck;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordСheck() {
        return passwordСheck;
    }

    public void setPasswordСheck(String passwordСheck) {
        this.passwordСheck = passwordСheck;
    }
}
