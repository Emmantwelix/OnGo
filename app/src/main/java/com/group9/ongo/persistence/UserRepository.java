package com.group9.ongo.persistence;

import com.group9.ongo.models.User;

public interface UserRepository {
    User getUserById(int userId);
    boolean CreateUser(String name, String email, int phone);
    boolean UpdateUser(int userId);
    boolean DeleteUser(int userId);

}
