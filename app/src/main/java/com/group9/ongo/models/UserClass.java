package com.group9.ongo.models;

public class UserClass{
    private int userId;
    private String username;
    private String email;
    private int phone;

    public UserClass(int userId, String username, String email, int phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.userId = userId;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public int getPhone() { return phone; }
}
