package com.group9.ongo.business.services;

import com.group9.ongo.business.validation.UserValidator;
import com.group9.ongo.models.User;
import com.group9.ongo.persistence.UserRepository;

public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public User getUserById(int userId) {
        return repo.getUserById(userId);
    }
    @Override
    public boolean CreateUser(String name, String email, int phone) {
        UserValidator.validateNewUser(name, email, phone);
        return repo.CreateUser(name, email, phone);
    }
    @Override
    public boolean DeleteUser(int userId) {
        return repo.DeleteUser(userId);
    }
}
