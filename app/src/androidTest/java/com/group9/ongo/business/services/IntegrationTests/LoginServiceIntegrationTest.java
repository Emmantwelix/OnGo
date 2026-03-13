package com.group9.ongo.business.services.IntegrationTests;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.group9.ongo.business.services.Implementations.LoginServiceImpl;
import com.group9.ongo.business.services.Interfaces.LoginService;
import com.group9.ongo.persistence.UserRepository;
import com.group9.ongo.persistence.real.AppDbHelper;
import com.group9.ongo.persistence.real.SqlUserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginServiceIntegrationTest {

    private UserRepository userRepo;
    private LoginService loginService;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();

        // fresh DB for each test
        context.deleteDatabase(AppDbHelper.DB_NAME);
        AppDbHelper dbHelper = new AppDbHelper(context, false);

        userRepo = new SqlUserRepository(dbHelper);
        loginService = new LoginServiceImpl(userRepo);
    }

    @Test
    public void login_whenUserExists_returnsUserId() {
        // Arrange
        String username = "John";
        String email = "john@email.com";
        String phone = "2045566812";

        int createdUserId = userRepo.addUser(username, email, phone);

        // Act
        int result = loginService.login(username, email);

        // Assert
        assertEquals(createdUserId, result);
    }

    @Test
    public void login_whenUserDoesNotExist_returnsNegativeOne() {
        // Arrange
        String username = "Jane";
        String email = "jane@email.com";

        // Act
        int result = loginService.login(username, email);

        // Assert
        assertEquals(-1, result);
    }
}
