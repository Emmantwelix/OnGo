package com.group9.ongo.persistence;

import com.group9.ongo.models.User;

public interface UserRepository {
    User getUserById(int userId);
    boolean CreateUser(String name, String email, int phone);
    boolean DeleteUser(int userId);
}
