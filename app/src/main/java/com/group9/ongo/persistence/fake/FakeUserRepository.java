package com.group9.ongo.persistence.fake;

import com.group9.ongo.models.FlightClass;
import com.group9.ongo.models.UserClass;
import com.group9.ongo.persistence.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class FakeUserRepository implements UserRepository {
    private final List<UserClass> users = new ArrayList<>();

    public FakeUserRepository() {
        users.add(new UserClass(1, "JohnDoe", "john@example.com", 1234567890));
    }

    @Override
    public UserClass getUserById(int userId) {
        for (UserClass user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }
}
