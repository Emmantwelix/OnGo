package com.group9.ongo.business.services;

import com.group9.ongo.models.UserClass;
import com.group9.ongo.persistence.UserRepository;

public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserClass getUserById(int userId) {
        return repo.getUserById(userId);
    }
}
