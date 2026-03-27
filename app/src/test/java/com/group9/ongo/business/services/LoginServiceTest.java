package com.group9.ongo.business.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group9.ongo.business.services.Implementations.LoginServiceImpl;
import com.group9.ongo.business.services.Interfaces.LoginService;
import com.group9.ongo.persistence.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginServiceTest {

    @Mock
    private UserRepository userRepo;

    private LoginService loginService;

    @Before
    public void setUp() {
        loginService = new LoginServiceImpl(userRepo);
    }

    @Test
    public void login_whenUserExists_returnsUserId() {
        // Arrange
        String username = "John";
        String email = "john@email.com";
        int expectedUserId = 5;

        when(userRepo.findUserIDByEmailAndName(username, email)).thenReturn(expectedUserId);

        // Act
        int result = loginService.login(username, email);

        // Assert
        assertEquals(expectedUserId, result);
        verify(userRepo).findUserIDByEmailAndName(username, email);
    }

    @Test
    public void login_whenUserDoesNotExist_returnsNegativeOne() {
        // Arrange
        String username = "Jane";
        String email = "jane@email.com";

        when(userRepo.findUserIDByEmailAndName(username, email)).thenReturn(-1);

        // Act
        int result = loginService.login(username, email);

        // Assert
        assertEquals(-1, result);
        verify(userRepo).findUserIDByEmailAndName(username, email);
    }
}