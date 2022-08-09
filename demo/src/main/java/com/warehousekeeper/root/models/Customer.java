package com.warehousekeeper.root.models;


import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
/**
 * @author Dmytro
 * @version 1.0
 * This class used as entity of table of DB. It describes all customers of Storage
 */
@Entity
@Table(name = "Customer")
public class Customer {
    /**
     * This is int field of DB where store id
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**
     * This is String field where store full name
     */
    @Column(name = "full_name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    private String fullName;
    /**
     * This is int field where store year of birth
     */
    @Column(name = "year_of_birth")
    @NotNull(message = "Year of birth should not be empty")
    @Min(value = 1951, message = "Year of birth should be greater than 1951")
    private int yearOfBirth;
    /**
     * This is int field where store phone number
     */
    @Column(name = "phone_number")
    @NotNull(message = "Year of birth should not be empty")
    private int phoneNumber;
    /**
     * This is int field where store email
     */
    @Column(name = "email")
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "You should write valid email")
    private String email;
    /**
     * This is field storages where store list of storages
     */
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Storage> storages;


    public Customer() {
    }

    public Customer(String fullName, int yearOfBirth, int phoneNumber, String email) {
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public List<Storage> getStorages() {
        return storages;
    }

    public void setStorages(List<Storage> storages) {
        this.storages = storages;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
