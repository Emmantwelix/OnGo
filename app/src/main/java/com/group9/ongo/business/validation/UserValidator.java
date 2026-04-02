package com.group9.ongo.business.validation;

import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_EMAIL_ALREADY_USED;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_INVALID_EMAIL;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_INVALID_PHONE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_LONG_PASSWORD;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NAME_TO_LONG;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NAME_TO_SHORT;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NOT_FOUND;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_SHORT_PASSWORD;
import static com.group9.ongo.business.constants.UserConstants.MAX_PASSWORD_LENGTH;
import static com.group9.ongo.business.constants.UserConstants.MIN_PASSWORD_LENGTH;

import com.group9.ongo.models.User;

public class UserValidator {
    private static final int MAX_LENGTH_NAME = 10;
    private static final int MIN_LENGTH_NAME = 3;
    private static final int LENGTH_PHONE = 10;
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static void validate(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException(USER_NOT_FOUND);
        }
    }
    
    public static void validateNewUser(String name, String email, String phone, String password, int userId) throws ValidationException {
        if (name == null || name.length() < MIN_LENGTH_NAME) {
            throw new ValidationException(USER_NAME_TO_SHORT);
        }
        else if (name.length() > MAX_LENGTH_NAME) {
            throw new ValidationException(USER_NAME_TO_LONG);
        }
        else if (phone.length() != LENGTH_PHONE) {
            throw new ValidationException(USER_INVALID_PHONE);
        }
        else if (!email.matches(EMAIL_REGEX)) {
            throw new ValidationException(USER_INVALID_EMAIL);
        }
        else if (password.length() < MIN_PASSWORD_LENGTH )
        {
            throw new ValidationException(USER_SHORT_PASSWORD);
        }
        else if (password.length() > MAX_PASSWORD_LENGTH)
        {
            throw new ValidationException(USER_LONG_PASSWORD);
        }
        else if ( userId != -1)
        {
            throw new ValidationException(USER_EMAIL_ALREADY_USED);
        }
    }

}
