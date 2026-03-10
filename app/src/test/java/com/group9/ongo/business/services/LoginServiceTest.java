package com.group9.ongo.business.services;

import com.group9.ongo.business.services.Implementations.LoginServiceImpl;
import com.group9.ongo.business.services.Interfaces.LoginService;
import com.group9.ongo.persistence.fake.FakeUserRepository;

import org.junit.Before;

public class LoginServiceTest {

    private LoginService loginService;

    @Before
    public void setUp() {
        loginService = new LoginServiceImpl(new FakeUserRepository());
    }

}
