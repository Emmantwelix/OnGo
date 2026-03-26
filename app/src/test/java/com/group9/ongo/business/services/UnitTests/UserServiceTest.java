package com.group9.ongo.business.services.UnitTests;

import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_DELETE_ERROR;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_INVALID_EMAIL;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_INVALID_PHONE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_LONG_PASSWORD;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NAME_TO_LONG;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NAME_TO_SHORT;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NOT_FOUND;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_SHORT_PASSWORD;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.group9.ongo.business.services.Implementations.UserServiceImpl;
import com.group9.ongo.business.services.Interfaces.UserService;
import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.User;
import com.group9.ongo.persistence.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    private static final String VALID_NAME = "John Doe";
    private static final String WEIRD_VALID_NAME = "c$$2s";
    private static final String SHORT_NAME = "";
    private static final String LONG_NAME = "ThisNameIsTooLong";
    private static final String VALID_EMAIL = "johnD@gmail.com";
    private static final String WEIRD_VALID_EMAIL = "X@Y.Z";
    private static final String INVALID_EMAIL = "@.";
    private static final String VALID_PHONE = "2045566812";
    private static final String INVALID_PHONE = "14203567823";
    private static final String VALID_PASSWORD = "password";
    private static final String SHORT_PASSWORD = "p";
    private static final String LONG_PASSWORD = "thispasswordiswaytoolongforoursystem";


    @Mock
    private UserRepository userRepository;

    private UserService userService;

    @Before
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void createUser_whenValidInput_returnsUserId() throws ValidationException {
        // Arrange
        int expectedUserId = 1;
        when(userRepository.addUser(VALID_NAME, VALID_EMAIL, VALID_PHONE, VALID_PASSWORD)).thenReturn(expectedUserId);
        when(userRepository.findUserIDByEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)).thenReturn(-1);
        // Act
        int result = userService.createUser(VALID_NAME, VALID_EMAIL, VALID_PHONE, VALID_PASSWORD);

        // Assert
        assertEquals(expectedUserId, result);
        verify(userRepository).addUser(VALID_NAME, VALID_EMAIL, VALID_PHONE, VALID_PASSWORD);
    }

    @Test
    public void createUser_whenWeirdButValidName_returnsUserId() throws ValidationException {
        // Arrange
        int expectedUserId = 2;
        when(userRepository.addUser(WEIRD_VALID_NAME, VALID_EMAIL, VALID_PHONE, VALID_PASSWORD)).thenReturn(expectedUserId);
        when(userRepository.findUserIDByEmailAndPassword(VALID_EMAIL, VALID_PASSWORD)).thenReturn(-1);

        // Act
        int result = userService.createUser(WEIRD_VALID_NAME, VALID_EMAIL, VALID_PHONE, VALID_PASSWORD);

        // Assert
        assertEquals(expectedUserId, result);
        verify(userRepository).addUser(WEIRD_VALID_NAME, VALID_EMAIL, VALID_PHONE, VALID_PASSWORD);
    }

    @Test
    public void createUser_whenNameTooLong_throwsValidationException() {
        // Arrange / Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(LONG_NAME, VALID_EMAIL, VALID_PHONE, VALID_PASSWORD)
        );

        // Assert
        assertEquals(USER_NAME_TO_LONG, exception.getMessage());
        verify(userRepository, never()).addUser(LONG_NAME, VALID_EMAIL, VALID_PHONE, VALID_PASSWORD);
    }

    @Test
    public void createUser_whenNameTooShort_throwsValidationException() {
        // Arrange / Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(SHORT_NAME, VALID_EMAIL, VALID_PHONE, VALID_PASSWORD)
        );

        // Assert
        assertEquals(USER_NAME_TO_SHORT, exception.getMessage());
        verify(userRepository, never()).addUser(SHORT_NAME, VALID_EMAIL, VALID_PHONE, VALID_PASSWORD);
    }

    @Test
    public void createUser_whenWeirdButValidEmail_returnsUserId() throws ValidationException {
        // Arrange
        int expectedUserId = 3;
        when(userRepository.addUser(VALID_NAME, WEIRD_VALID_EMAIL, VALID_PHONE, VALID_PASSWORD)).thenReturn(expectedUserId);
        when(userRepository.findUserIDByEmailAndPassword(WEIRD_VALID_EMAIL, VALID_PASSWORD)).thenReturn(-1);

        // Act
        int result = userService.createUser(VALID_NAME, WEIRD_VALID_EMAIL, VALID_PHONE, VALID_PASSWORD);

        // Assert
        assertEquals(expectedUserId, result);
        verify(userRepository).addUser(VALID_NAME, WEIRD_VALID_EMAIL, VALID_PHONE, VALID_PASSWORD);
    }

    @Test
    public void createUser_whenInvalidEmail_throwsValidationException() {
        // Arrange / Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(VALID_NAME, INVALID_EMAIL, VALID_PHONE, VALID_PASSWORD)
        );

        // Assert
        assertEquals(USER_INVALID_EMAIL, exception.getMessage());
        verify(userRepository, never()).addUser(VALID_NAME, INVALID_EMAIL, VALID_PHONE, VALID_PASSWORD);
    }

    @Test
    public void createUser_whenInvalidPhone_throwsValidationException() {
        // Arrange / Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(VALID_NAME, VALID_EMAIL, INVALID_PHONE, VALID_PASSWORD)
        );

        // Assert
        assertEquals(USER_INVALID_PHONE, exception.getMessage());
        verify(userRepository, never()).addUser(VALID_NAME, VALID_EMAIL, INVALID_PHONE, VALID_PASSWORD);
    }

    @Test
    public void createUser_whenPasswordTooShort_throwsValidationException() {
        // Arrange / Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(VALID_NAME, VALID_EMAIL, VALID_PHONE, SHORT_PASSWORD)
        );

        // Assert
        assertEquals(USER_SHORT_PASSWORD, exception.getMessage());
        verify(userRepository, never()).addUser(VALID_NAME, VALID_EMAIL, VALID_PHONE, SHORT_PASSWORD);
    }

    @Test
    public void createUser_whenPasswordTooLong_throwsValidationException() {
        // Arrange / Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(VALID_NAME, VALID_EMAIL, VALID_PHONE, LONG_PASSWORD)
        );

        // Assert
        assertEquals(USER_LONG_PASSWORD, exception.getMessage());
        verify(userRepository, never()).addUser(VALID_NAME, VALID_EMAIL, VALID_PHONE, LONG_PASSWORD);
    }

    @Test
    public void getUserById_whenUserExists_returnsUser() throws ValidationException {
        // Arrange
        int userId = 4;
        User user = new User(userId, VALID_NAME, VALID_EMAIL, VALID_PHONE, VALID_PASSWORD);
        when(userRepository.getUserById(userId)).thenReturn(user);

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertEquals(VALID_NAME, result.getUsername());
        assertEquals(VALID_EMAIL, result.getEmail());
        assertEquals(VALID_PHONE, result.getPhone());
        verify(userRepository).getUserById(userId);
    }

    @Test
    public void getUserById_whenUserMissing_throwsValidationException() {
        // Arrange
        int invalidUserId = 100;
        when(userRepository.getUserById(invalidUserId)).thenReturn(null);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.getUserById(invalidUserId)
        );

        // Assert
        assertEquals(USER_NOT_FOUND, exception.getMessage());
        verify(userRepository).getUserById(invalidUserId);
    }

    @Test
    public void deleteUser_whenUserExists_doesNotThrow() throws ValidationException {
        // Arrange
        int userId = 5;
        when(userRepository.deleteUser(userId)).thenReturn(true);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).deleteUser(userId);
    }

    @Test
    public void deleteUser_whenUserDoesNotExist_throwsValidationException() {
        // Arrange
        int invalidUserId = 100;
        when(userRepository.deleteUser(invalidUserId)).thenReturn(false);

        // Act
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.deleteUser(invalidUserId)
        );

        // Assert
        assertEquals(USER_DELETE_ERROR, exception.getMessage());
        verify(userRepository).deleteUser(invalidUserId);
    }
}
