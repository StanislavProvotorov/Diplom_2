package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.OrderModel;

import java.util.ArrayList;
import java.util.List;

import static data.OrderData.CREATE_ORDER;
import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Добавление ингредиента")
    public static void ingredientAdd(OrderModel order, String ingredient) {
        List<String> currentIngredients = order.getIngredients();
        if (currentIngredients != null) {
            currentIngredients.add(ingredient);
        } else {
            // На всякий случай, если список ещё не инициализирован
            List<String> newIngredients = new ArrayList<>();
            newIngredients.add(ingredient);
            order.setIngredients(newIngredients);
        }
    }

    @Step("Создание заказа c авторизацией")
    public static Response createOrder(OrderModel order, String token) {
        return given()
                .log().all()
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(CREATE_ORDER)
                .then()
                .extract().response();
    }

    @Step("Создание заказа без авторизации")
    public static Response createOrder(OrderModel order) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post(CREATE_ORDER)
                .then()
                .extract().response();
    }
}