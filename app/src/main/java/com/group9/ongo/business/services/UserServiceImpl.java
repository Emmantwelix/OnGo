package com.group9.ongo.business.services;

import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_DELETE_ERROR;

import com.group9.ongo.business.validation.UserValidator;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.User;
import com.group9.ongo.persistence.UserRepository;

public class UserServiceImpl implements UserService {
    private final UserRepository repo;

    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public User getUserById(int userId) throws ValidationException {
        User user = repo.getUserById(userId);
        UserValidator.validate(user);
        return user;
    }
    @Override
    public int createUser(String name, String email, String phone) throws ValidationException {
        UserValidator.validateNewUser(name, email, phone);
        return repo.addUser(name, email, phone);
    }
    @Override
    public boolean deleteUser(int userId) throws ValidationException {
        boolean success = repo.deleteUser(userId);
        if ( success )
        {
            return true;
        }
        else
        {
           throw new ValidationException(USER_DELETE_ERROR);
        }
    }
}
