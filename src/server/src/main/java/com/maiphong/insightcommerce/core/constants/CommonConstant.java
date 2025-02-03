package com.maiphong.insightcommerce.core.constants;

@lombok.experimental.UtilityClass
public final class CommonConstant {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ADMIN = "Admin";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";

    // Error messages
    // UsernameNotFoundException
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String ROLE_ALREADY_EXIST = "Role is already exist";
}
