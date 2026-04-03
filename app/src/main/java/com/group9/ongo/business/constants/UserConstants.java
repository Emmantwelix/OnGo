package com.group9.ongo.business.constants;

public class UserConstants {
    private UserConstants() {}
    public static final int MAX_LENGTH_NAME = 50;
    public static final int MIN_LENGTH_NAME = 3;
    public static final int LENGTH_PHONE = 10;
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String SAMPLE_USER_NAME = "john doe";
    public static final String SAMPLE_USER_EMAIL = "johnd@example.com";
    public static final String SAMPLE_USER_PHONE_NUM = "2042345433";
    public static final int MIN_PASSWORD_LENGTH = 3;
    public static final int MAX_PASSWORD_LENGTH = 64;
    public static final String SAMPLE_USER_PASSWORD = "12345678";
}
