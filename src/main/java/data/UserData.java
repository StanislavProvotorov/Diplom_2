package data;

import com.github.javafaker.Faker;

public class UserData {
    public static final String BASE_URL = "https://stellarburgers.education-services.ru";
    public static final String CREATE_USER_PATH = "/api/auth/register";
    public static final String LOGIN_USER_PATH = "/api/auth/login";
    public static final String LOGOUT_USER_PATH = "/api/auth/logout";
    public static final String INFO_USER_PATH = "/api/auth/user";

    static Faker user = new Faker();
    public static final String NAME = user.name().lastName();
    public static final String EMAIL = user.name().lastName() + user.regexify("[0-9]{8}") + "@yandex.ru";
    public static final String PASSWORD = user.regexify("[0-9]{8}");
}