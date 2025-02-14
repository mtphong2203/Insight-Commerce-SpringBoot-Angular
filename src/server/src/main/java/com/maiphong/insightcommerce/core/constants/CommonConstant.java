package com.maiphong.insightcommerce.core.constants;

@lombok.experimental.UtilityClass
public final class CommonConstant {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_TOKEN_PREFIX = "Bearer ";

    // Error messages
    public static final String USER_NOT_FOUND = "User not found";
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String CITY_NOT_FOUND = "City not found";
    public static final String MOVIE_FORMAT_NOT_FOUND = "Movie format not found";
    public static final String GENRE_NOT_FOUND = "Genre not found";
    public static final String SHOW_TIME_NOT_FOUND = "Show time not found";
    public static final String CINEMA_NOT_FOUND = "Cinema not found";
    public static final String ROOM_NOT_FOUND = "Room not found";
    public static final String SEAT_NOT_FOUND = "Seat not found";
    public static final String MOVIE_NOT_FOUND = "Movie not found";

    // Filter names
    public static final String DELETED_FILTER = "deletedFilter";
}
