package com.group9.ongo.business.services;

import com.group9.ongo.models.User;

public interface UserService {
    User getUserById(int userId);
    int createUser(String name, String email, String phone); //return user id
    boolean deleteUser(int userId);
}
