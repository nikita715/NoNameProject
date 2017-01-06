package ru.ifmo.shelf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by nikge on 02.01.2017.
 */
@Entity
@Table(name = "PRIORITY")
public class Priority {

    @Id
    @Column(name = "ID")
    private int id;
}
