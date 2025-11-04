package com.example.server.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true, length = 50)
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "age")
    private Integer age;

    protected User() {
    }

    private User(String userId, String password, String name, Integer age) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.age = age;
    }

    public static User create(String userId, String password, String name, Integer age) {
        return new User(userId, password, name, age);
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void changeProfile(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
