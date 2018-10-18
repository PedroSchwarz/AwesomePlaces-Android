package com.rodrigues.pedroschwarz.awesomeplaces.model;

import com.google.firebase.firestore.Exclude;

import java.util.Calendar;

public class User {

    private String id;
    private String username;
    private String email;
    private String password;
    private String image;
    private Long createdAt;

    public User() {
    }

    public User(String id, String username, String email, String password, String image) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
        this.createdAt = Calendar.getInstance().getTimeInMillis();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
