package com.group9.ongo.business.validation;

import com.group9.ongo.models.User;

public class UserValidator {
    private static final int MAX_LENGTH_NAME = 10;
    private static final int MIN_LENGTH_NAME = 4;
    private static final int MAX_LENGTH_EMAIL = 20;
    private static final int MIN_LENGTH_EMAIL = 5;
    private static final int MAX_LENGTH_PHONE = 999999999;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static void validate(User user) {
        if (user == null) {
            throw new ValidationException("User cannot be null");
        }
    }
    
    public static void validateNewUser(String name, String email, int phone) {
        if (name == null || name.length() < MIN_LENGTH_NAME) {
            throw new ValidationException("Name is to short");
        }
        else if (name.length() > MAX_LENGTH_NAME) {
            throw new ValidationException("Name is to long");
        }
        else if (email == null || email.length() < MIN_LENGTH_EMAIL) {
            throw new ValidationException("Email is to short");
        }
        else if (email.length() > MAX_LENGTH_EMAIL) {
            throw new ValidationException("Email is to long");
        }
        else if (phone < 0) {
            throw new ValidationException("Invalid phone number");
        }
        else if (phone > MAX_LENGTH_PHONE) {
            throw new ValidationException("Invalid phone number");
        }
        else if (!email.matches(EMAIL_REGEX)) {
            throw new ValidationException("Invalid email format");
        }
    }

}
