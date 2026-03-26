package com.group9.ongo.persistence;

import com.group9.ongo.models.User;

public interface UserRepository {
    User getUserById(int userId);
    int addUser(String name, String email, String phone, String password);
    boolean deleteUser(int userId);

    int findUserIDByEmailAndPassword(String name, String password);
}
