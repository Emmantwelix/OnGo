package com.group9.ongo.persistence.fake;

import com.group9.ongo.models.User;
import com.group9.ongo.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private static int nextUserId = 1;


    public FakeUserRepository() {
        this.addUser("john doe", "johnd@example.com", "2042345433");
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
    public int addUser(String name, String email, String phone) {
        User user = new User(nextUserId, name, email, phone);
        users.add(user);
        nextUserId++;
        return user.getUserId();
    }

    @Override
    public boolean deleteUser(int userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                users.remove(user);
                return true;
            }
        }
        return false;
    }
}
