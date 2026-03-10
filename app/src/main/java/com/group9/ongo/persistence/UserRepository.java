package com.group9.ongo.persistence;

import com.group9.ongo.models.User;

public interface UserRepository {
    User getUserById(int userId);
    int addUser(String name, String email, String phone);
    boolean deleteUser(int userId);

    int findUserIDByEmailAndName(String name, String email);
}
