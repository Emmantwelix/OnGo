package com.group9.ongo.persistence.fake;

import com.group9.ongo.models.User;
import com.group9.ongo.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private static int nextUserId = 1;


    public FakeUserRepository() {
        users.add(new User(1, "JohnDoe", "john@example.com", 123456789));
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
    @Override
    public boolean CreateUser(String name, String email, int phone) {
        User user = new User(nextUserId, name, email, phone);
        nextUserId++;
        return users.add(user);
    }

    @Override
    public boolean DeleteUser(int userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                users.remove(user);
                return true;
            }
        }
        return false;
    }
}
