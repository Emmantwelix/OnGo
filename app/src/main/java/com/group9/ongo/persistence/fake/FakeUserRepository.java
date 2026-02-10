package com.group9.ongo.persistence.fake;

import com.group9.ongo.models.User;
import com.group9.ongo.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();

    public FakeUserRepository() {
        users.add(new User(1, "JohnDoe", "john@example.com", 1234567890));
    }

    @Override
    public User getUserById(int userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }
}
