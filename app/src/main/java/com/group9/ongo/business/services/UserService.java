package com.group9.ongo.business.services;

import com.group9.ongo.models.User;

public interface UserService {
    User getUserById(int userId);
    boolean CreateUser(String name, String email, int phone);
    boolean UpdateUser(int userId);
    boolean DeleteUser(int userId);

}
