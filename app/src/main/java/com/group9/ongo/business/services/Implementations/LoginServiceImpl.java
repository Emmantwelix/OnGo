package com.group9.ongo.business.services.Implementations;

import com.group9.ongo.business.services.Interfaces.LoginService;
import com.group9.ongo.persistence.UserRepository;

public class LoginServiceImpl implements LoginService {
    UserRepository userRepo;
    public LoginServiceImpl(UserRepository userRepo)
    {
        this.userRepo = userRepo;
    }

    @Override
    public int login(String email, String password)
    {
        return userRepo.findUserIDByEmailAndPassword(email, password);
    }
}
