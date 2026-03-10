package com.group9.ongo.persistence.fake;

import static com.group9.ongo.business.constants.UserConstants.SAMPLE_USER_EMAIL;
import static com.group9.ongo.business.constants.UserConstants.SAMPLE_USER_NAME;
import static com.group9.ongo.business.constants.UserConstants.SAMPLE_USER_PHONE_NUM;

import com.group9.ongo.models.User;
import com.group9.ongo.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeUserRepository implements UserRepository {
    private final List<User> users = new ArrayList<>();
    private static int nextUserId = 1;


    public FakeUserRepository() {
        this.addUser(SAMPLE_USER_NAME, SAMPLE_USER_EMAIL, SAMPLE_USER_PHONE_NUM);
        this.addUser(SAMPLE_USER_NAME+"2", SAMPLE_USER_EMAIL+"2", SAMPLE_USER_PHONE_NUM);
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

    @Override
    public int findUserIDByEmailAndName(String name, String email) {
        for (User user : users) {
            if (user.getUsername().equals(name) && user.getEmail().equals(email)) {
                return user.getUserId();
            }
        }
        return -1;
    }

}
