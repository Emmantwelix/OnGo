package com.group9.ongo.business.services.Interfaces;

import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.User;

public interface UserService {
    User getUserById(int userId) throws ValidationException;
    int createUser(String name, String email, String phone, String password) throws ValidationException; //return user id
    void deleteUser(int userId) throws ValidationException;
}
