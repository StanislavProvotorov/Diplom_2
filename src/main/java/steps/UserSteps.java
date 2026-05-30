package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.UserModel;

import static data.UserData.*;
import static io.restassured.RestAssured.given;

public class UserSteps {

    @Step("Создаем пользователя")
    public static Response createUser(UserModel user) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(CREATE_USER_PATH)
                .then()
                .extract().response();
    }

    @Step("Получаем токен access")
    public static String accessToken(Response response) {
        String tokenWithBearer = response.jsonPath().getString("accessToken");
        return tokenWithBearer.replace("Bearer ", "");
    }

    @Step("Получаем токен refresh")
    public static String refreshToken(Response response) {
        return response.jsonPath().getString("refreshToken");
    }

    @Step("Авторизация пользователя в системе")
    public static Response loginSystemUser(UserModel user) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(LOGIN_USER_PATH)
                .then()
                .extract().response();
    }

    @Step("Удаляем пользователя")
    public static void deleteUser(String token) {
        given()
                .log().all()
                .auth().oauth2(token)
                .when()
                .delete(INFO_USER_PATH)
                .then()
                .log().all();
    }
}