package com.group9.ongo.business.services.UnitTests;

import static com.group9.ongo.business.constants.UserConstants.SAMPLE_USER_NAME;
import static com.group9.ongo.business.constants.UserConstants.SAMPLE_USER_PASSWORD;
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
        int expectedUserId = 5;

        when(userRepo.findUserIDByEmailAndPassword(SAMPLE_USER_NAME, SAMPLE_USER_PASSWORD)).thenReturn(expectedUserId);

        // Act
        int result = loginService.login(SAMPLE_USER_NAME, SAMPLE_USER_PASSWORD);

        // Assert
        assertEquals(expectedUserId, result);
        verify(userRepo).findUserIDByEmailAndPassword(SAMPLE_USER_NAME, SAMPLE_USER_PASSWORD);
    }

    @Test
    public void login_whenUserDoesNotExist_returnsNegativeOne() {
        // Arrange
        String password = "Jane";
        String email = "jane@email.com";

        when(userRepo.findUserIDByEmailAndPassword(email, password)).thenReturn(-1);

        // Act
        int result = loginService.login(email, password);

        // Assert
        assertEquals(-1, result);
        verify(userRepo).findUserIDByEmailAndPassword(email, password);
    }
}