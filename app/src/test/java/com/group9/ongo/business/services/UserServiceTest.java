package com.group9.ongo.business.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.group9.ongo.business.validation.ValidationException;
import com.group9.ongo.models.User;
import com.group9.ongo.persistence.PassengerRepository;
import com.group9.ongo.persistence.UserRepository;
import com.group9.ongo.persistence.fake.FakeUserRepository;

import org.junit.Before;
import org.junit.Test;

public class UserServiceTest {
    private final String VALID_NAME = "John Doe";
    private final String WEIRD_VALID_NAME = "c$$2s";
    private final String SHORT_NAME = "";
    private final String LONG_NAME = "ThisNameIsTooLong";
    private final String VALID_EMAIL = "johnD@gmail.com";
    private final String WEIRD_VALID_EMAIL = "X@Y.Z";
    private final String INVALID_EMAIL = "@.";
    private final String VALID_PHONE = "2045566812";
    private final String INVALID_PHONE = "14203567823";

    private UserService userService;
    private UserRepository userRepository;


    @Before
    public void setUp() {
        userRepository = new FakeUserRepository();
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    public void testCreateValidUser_returnsUserId() throws ValidationException {
        //arrange + act
        int userId = userService.createUser(VALID_NAME, VALID_EMAIL, VALID_PHONE);
        //assert
        User user = userService.getUserById(userId);
        assertNotNull(user);
        assertEquals(VALID_NAME, user.getUsername());
        assertEquals(VALID_EMAIL, user.getEmail());
        assertEquals(VALID_PHONE, user.getPhone());
        assertEquals(userId, user.getUserId());
    }

    @Test
    public void testCreateUserWithWeirdName_retrunsUserId() throws ValidationException {
        //arrange + act
        int userId = userService.createUser(WEIRD_VALID_NAME, VALID_EMAIL, VALID_PHONE);
        //assert
        User user = userService.getUserById(userId);
        assertNotNull(user);
        assertEquals(userId, user.getUserId());
    }

    @Test
    public void testCreateUser_withInvalidLongName_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(LONG_NAME, VALID_EMAIL, VALID_PHONE)
        );
        assertEquals("Name is too long", exception.getMessage());
    }

    @Test
    public void testCreateUser_withInvalidShortName_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(SHORT_NAME, VALID_EMAIL, VALID_PHONE)
        );
        assertEquals("Name is too short", exception.getMessage());
    }

    @Test
    public void testCreateUser_withWeirdEmail_returnsUserId() throws ValidationException {
        //arrange + act
        int userId = userService.createUser(VALID_NAME, WEIRD_VALID_EMAIL, VALID_PHONE);
        //assert
        User user = userService.getUserById(userId);
        assertNotNull(user);
        assertEquals(userId, user.getUserId());
    }

    @Test
    public void testCreateUser_withInvalidEmail_throwsException() throws ValidationException {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(VALID_NAME, INVALID_EMAIL, VALID_PHONE)
        );
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    public void testCreateUser_withInvalidPhone_throwsException() throws ValidationException {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.createUser(VALID_NAME, VALID_EMAIL, INVALID_PHONE)
        );
        assertEquals("Invalid phone number, should be 10 digits", exception.getMessage());
    }

    @Test
    public void testGetUserByID_withValidID_returnsUser() throws ValidationException {
        //arrange
        userService.createUser("tom", "tom@gmail.com", "4325329946");
        userService.createUser("bob", "bob@gmail.com", "5203028203");
        userService.createUser("jerry", "jerry@gmail.com", "4325329946");
        //act
        int userId = userService.createUser(VALID_NAME, VALID_EMAIL, VALID_PHONE);
        User user = userService.getUserById(userId);
        //assert
        assertNotNull(user);
        assertEquals(VALID_NAME, user.getUsername());
        assertEquals(VALID_EMAIL, user.getEmail());
        assertEquals(VALID_PHONE, user.getPhone());
        assertEquals(userId, user.getUserId());
    }

    @Test
    public void testGetUserByID_withInvalidID_returnsNull() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.getUserById(100)
        );
        assertEquals("User not found", exception.getMessage());
    }

    @Test
    public void testDeleteUser_withValidID_returnsTrue() throws ValidationException {
        //arrange
        int userId = userService.createUser(VALID_NAME, VALID_EMAIL, VALID_PHONE);
        //act
        boolean success = userService.deleteUser(userId);
        //assert
        assertTrue(success);
    }

    @Test
    public void testDeleteUser_withInvalidID_throwsException() {
        //arrange + act + assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> userService.deleteUser(100)
        );
        assertEquals("User could not be deleted, since user does not exist", exception.getMessage());
    }




}
