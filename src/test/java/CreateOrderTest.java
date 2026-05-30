import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.OrderModel;
import model.UserModel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static data.OrderData.*;
import static data.UserData.*;
import static java.net.HttpURLConnection.*;
import static steps.OrderSteps.createOrder;
import static steps.OrderSteps.ingredientAdd;
import static steps.UserSteps.*;

public class CreateOrderTest extends BaseApiTest {
    private UserModel user;
    private Response responseUser;
    private OrderModel order;
    private String token;

    @Before
    public void setUpUser() {
        user = new UserModel(EMAIL, PASSWORD, NAME);
        responseUser = createUser(user);
        token = accessToken(responseUser);
    }

    @Test
    @DisplayName("Успешное создание заказа с авторизацией")
    @Description("Запрос возвращает 200_OK, заказ создан")
    public void testCreatedOrder() {
        order = new OrderModel(new ArrayList<>());
        ingredientAdd(order, BUN);
        ingredientAdd(order, SAUCE);
        ingredientAdd(order, MAIN);

        String actual = createOrder(order, token)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .extract()
                .path("order.status");

        Assert.assertEquals("Заказ не создан", "done", actual);
    }

    @Test
    @DisplayName("Успешное создание заказа без авторизации")
    @Description("Запрос возвращает 200_OK, заказ создан")
    public void testCreatedOrderNoUser() {
        order = new OrderModel(new ArrayList<>());
        ingredientAdd(order, BUN);
        ingredientAdd(order, SAUCE);
        ingredientAdd(order, MAIN);

        boolean actual = createOrder(order)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .extract()
                .path("success");

        Assert.assertTrue("Заказ не создан", actual);
    }

    @Test
    @DisplayName("Успешное создание заказа с ингредиентами")
    @Description("Запрос возвращает 200_OK, заказ создан")
    public void testCreatedOrderIngredients() {
        order = new OrderModel(new ArrayList<>());
        ingredientAdd(order, BUN);
        ingredientAdd(order, SAUCE);
        ingredientAdd(order, MAIN);

        List<String> actual = createOrder(order, token)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .extract()
                .path("order.ingredients");

        Assert.assertNotNull("Заказ не создан", actual);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Запрос возвращает 400_BAD_REQUEST, заказ не создан")
    public void testCreatedOrderNoIngredients() {
        order = new OrderModel(new ArrayList<>());

        String actual = createOrder(order, token)
                .then()
                .log().all()
                .statusCode(HTTP_BAD_REQUEST)
                .extract()
                .path("message");

        Assert.assertEquals("Заказ создан", "Ingredient ids must be provided", actual);
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Запрос возвращает 500_INTERNAL_ERROR, заказ не создан")
    public void testCreatedOrderIncorrectHashIngredients() {
        order = new OrderModel(new ArrayList<>());
        ingredientAdd(order, BUN + "55665");
        ingredientAdd(order, SAUCE + "56632");
        ingredientAdd(order, MAIN + "55445");

        int actual = createOrder(order, token)
                .then()
                .log().all()
                .extract()
                .statusCode();

        Assert.assertEquals("Заказ создан", 500, actual);
    }

    @After
    public void cleanUp() {
        try {
            deleteUser(token);
        } catch (Exception e) {
            System.out.println("Ошибка при очистке, токен: " + e.getMessage());
        }
    }
}