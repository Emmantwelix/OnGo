package com.group9.ongo.models;

public class UserClass{
    private static int num_users = 0; //used to determine user id
    private int userId;
    private String username;
    private String email;
    private int phone;

    public UserClass(int userId, String username, String email, int phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.userId = num_users;
        num_users++;
    }

    public int getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public int getPhone() { return phone; }
}
