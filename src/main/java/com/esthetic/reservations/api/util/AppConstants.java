package com.esthetic.reservations.api.util;

public class AppConstants {

    public static final String PAGE_NUMBER = "0";
    public static final String PAGE_SIZE = "2147483647";
    public static final String DEFAULT_SORT_ORDER = "id";
    public static final String DEFAULT_SORT_DIR = "asc";

    public static final String ADMIN_ROLE_NAME = "ADMIN";
    public static final String OWNER_ROLE_NAME = "OWNER";
    public static final String EMPLOYEE_ROLE_NAME = "EMPLOYEE";
    public static final String CLIENT_ROLE_NAME = "CLIENT";

    public static final String JWT_SECRET = "ersecret";

    public static final Long JWT_SECOND = 1000l;
    public static final Long JWT_MINUTE = JWT_SECOND * 60;
    public static final Long JWT_HOUR = JWT_MINUTE * 60;
    public static final Long JWT_DAY = JWT_HOUR * 24;
    public static final Long JWT_EXPIRATION_TIME = JWT_DAY * 30;

}
