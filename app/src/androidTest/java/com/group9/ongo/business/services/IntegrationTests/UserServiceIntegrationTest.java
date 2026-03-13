package com.group9.ongo.business.services.IntegrationTests;

import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_DELETE_ERROR;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_INVALID_EMAIL;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_INVALID_PHONE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NAME_TO_LONG;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NAME_TO_SHORT;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NOT_FOUND;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.group9.ongo.business.services.Implementations.UserServiceImpl;
import com.group9.ongo.business.services.Interfaces.UserService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.User;
import com.group9.ongo.persistence.UserRepository;
import com.group9.ongo.persistence.real.AppDbHelper;
import com.group9.ongo.persistence.real.SqlUserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserServiceIntegrationTest {

    private static final String VALID_NAME = "John Doe";
    private static final String WEIRD_VALID_NAME = "c$$2s";
    private static final String SHORT_NAME = "";
    private static final String LONG_NAME = "ThisNameIsTooLong";
    private static final String VALID_EMAIL = "johnD@gmail.com";
    private static final String WEIRD_VALID_EMAIL = "X@Y.Z";
    private static final String INVALID_EMAIL = "@.";
    private static final String VALID_PHONE = "2045566812";
    private static final String INVALID_PHONE = "14203567823";

    private UserRepository userRepository;
    private UserService userService;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        context.deleteDatabase(AppDbHelper.DB_NAME);

        AppDbHelper dbHelper = new AppDbHelper(context, false);
        userRepository = new SqlUserRepository(dbHelper);
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void createUser_whenValidInput_returnsUserId() throws ValidationException {
        int result = userService.createUser(VALID_NAME, VALID_EMAIL, VALID_PHONE);

        assertTrue(result > 0);

        User user = userService.getUserById(result);
        assertNotNull(user);
        assertEquals(result, user.getUserId());
        assertEquals(VALID_NAME, user.getUsername());
        assertEquals(VALID_EMAIL, user.getEmail());
        assertEquals(VALID_PHONE, user.getPhone());
    }

    @Test
    public void createUser_whenWeirdButValidName_returnsUserId() throws ValidationException {
        int result = userService.createUser(WEIRD_VALID_NAME, VALID_EMAIL, VALID_PHONE);

        assertTrue(result > 0);

        User user = userService.getUserById(result);
        assertNotNull(user);
        assertEquals(result, user.getUserId());
        assertEquals(WEIRD_VALID_NAME, user.getUsername());
        assertEquals(VALID_EMAIL, user.getEmail());
        assertEquals(VALID_PHONE, user.getPhone());
    }

    @Test
    public void createUser_whenNameTooLong_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(LONG_NAME, VALID_EMAIL, VALID_PHONE)
        );

        assertEquals(USER_NAME_TO_LONG, exception.getMessage());
    }

    @Test
    public void createUser_whenNameTooShort_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(SHORT_NAME, VALID_EMAIL, VALID_PHONE)
        );

        assertEquals(USER_NAME_TO_SHORT, exception.getMessage());
    }

    @Test
    public void createUser_whenWeirdButValidEmail_returnsUserId() throws ValidationException {
        int result = userService.createUser(VALID_NAME, WEIRD_VALID_EMAIL, VALID_PHONE);

        assertTrue(result > 0);

        User user = userService.getUserById(result);
        assertNotNull(user);
        assertEquals(result, user.getUserId());
        assertEquals(VALID_NAME, user.getUsername());
        assertEquals(WEIRD_VALID_EMAIL, user.getEmail());
        assertEquals(VALID_PHONE, user.getPhone());
    }

    @Test
    public void createUser_whenInvalidEmail_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(VALID_NAME, INVALID_EMAIL, VALID_PHONE)
        );

        assertEquals(USER_INVALID_EMAIL, exception.getMessage());
    }

    @Test
    public void createUser_whenInvalidPhone_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(VALID_NAME, VALID_EMAIL, INVALID_PHONE)
        );

        assertEquals(USER_INVALID_PHONE, exception.getMessage());
    }

    @Test
    public void getUserById_whenUserExists_returnsUser() throws ValidationException {
        int userId = userService.createUser(VALID_NAME, VALID_EMAIL, VALID_PHONE);

        User result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(VALID_NAME, result.getUsername());
        assertEquals(VALID_EMAIL, result.getEmail());
        assertEquals(VALID_PHONE, result.getPhone());
    }

    @Test
    public void getUserById_whenUserMissing_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.getUserById(100)
        );

        assertEquals(USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void deleteUser_whenUserExists_doesNotThrow() throws ValidationException {
        int userId = userService.createUser(VALID_NAME, VALID_EMAIL, VALID_PHONE);

        userService.deleteUser(userId);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.getUserById(userId)
        );

        assertEquals(USER_NOT_FOUND, exception.getMessage());
    }

    @Test
    public void deleteUser_whenUserDoesNotExist_throwsValidationException() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.deleteUser(100)
        );

        assertEquals(USER_DELETE_ERROR, exception.getMessage());
    }
}