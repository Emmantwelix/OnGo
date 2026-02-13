package com.group9.ongo.business.validation;

import com.group9.ongo.models.User;

public class UserValidator {
    private static final int MAX_LENGTH_NAME = 10;
    private static final int MIN_LENGTH_NAME = 3;
    private static final int LENGTH_PHONE = 10;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static void validate(User user) {
        if (user == null) {
            throw new ValidationException("User cannot be found");
        }
    }
    
    public static void validateNewUser(String name, String email, String phone) {
        if (name == null || name.length() < MIN_LENGTH_NAME) {
            throw new ValidationException("Name is to short");
        }
        else if (name.length() > MAX_LENGTH_NAME) {
            throw new ValidationException("Name is to long");
        }
        else if (phone.length() != LENGTH_PHONE) {
            throw new ValidationException("Invalid phone number, should be 10 digits");
        }
        else if (!email.matches(EMAIL_REGEX)) {
            throw new ValidationException("Invalid email format");
        }
    }

}
