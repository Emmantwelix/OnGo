package com.group9.ongo.business.validation;

import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_INVALID_EMAIL;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_INVALID_PHONE;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NAME_TO_LONG;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NAME_TO_SHORT;
import static com.group9.ongo.business.constants.ErrorMessageConstants.USER_NOT_FOUND;
import static com.group9.ongo.business.constants.UserConstants.EMAIL_REGEX;
import static com.group9.ongo.business.constants.UserConstants.LENGTH_PHONE;
import static com.group9.ongo.business.constants.UserConstants.MAX_LENGTH_NAME;
import static com.group9.ongo.business.constants.UserConstants.MIN_LENGTH_NAME;

import com.group9.ongo.models.User;

public class UserValidator {
    public static void validate(User user) {
        if (user == null) {
            throw new ValidationException(USER_NOT_FOUND);
        }
    }
    
    public static void validateNewUser(String name, String email, String phone) {
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
    }

}
