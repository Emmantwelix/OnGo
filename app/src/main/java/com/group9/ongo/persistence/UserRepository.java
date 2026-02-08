package com.group9.ongo.persistence;

import com.group9.ongo.models.UserClass;

public interface UserRepository {
    UserClass getUserById(int userId);
}
