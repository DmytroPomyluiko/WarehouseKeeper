package com.warehousekeeper.root.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
/**
 * @author Dmytro
 * @version 1.0
 * This class uses as entity of table of DB. It describes Person table
 */
@Entity
@Table(name = "Person")
public class Person {
    /**
     * This is int field where store id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * This is string field where store username
     */
    @Column(name = "username")
    @NotEmpty(message = "Login should not be empty")
    @Size(min = 3, max = 100, message = "Login should be between 2 and 100 characters")
    private String username;
    /**
     * This is string field where store password
     */
    @Column(name = "password")
    private String password;
    /**
     * This is string field  where store role of user for authorization
     */
    @Column(name = "role")
    private String role;

    public Person() {
    }

    public Person(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
