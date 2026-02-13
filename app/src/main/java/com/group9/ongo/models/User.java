package com.group9.ongo.models;

public class User {
    private int userId;
    private String username;
    private String email;
    private String phone;

    public User(int userId, String username, String email, String phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
