import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.UserModel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import static data.UserData.*;
import static java.net.HttpURLConnection.HTTP_FORBIDDEN;
import static java.net.HttpURLConnection.HTTP_OK;
import static steps.UserSteps.*;

public class CreateUserTest extends BaseApiTest {
    private UserModel user;
    private Response response;

    @Test
    @DisplayName("Успешная регистрация уникального пользователя")
    @Description("Запрос возвращает 200_OK, пользователь зарегистрирован")
    public void testCreatedUser() {
        user = new UserModel(EMAIL, PASSWORD, NAME);
        response = createUser(user);

        boolean actual = response
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .extract()
                .path("success");

        Assert.assertTrue("Пользователь не создан", actual);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Запрос возвращает 403_Forbidden, пользователь не зарегистрирован")
    public void testCreatedTwoUserRegistered() {
        user = new UserModel(EMAIL, PASSWORD, NAME);
        response = createUser(user);

        boolean actual = createUser(user)
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .path("success");

        Assert.assertFalse("Создан пользователь, зарегистрированый в системе", actual);
    }

    @Test
    @DisplayName("Создание пользователя, не заполняя поле Email")
    @Description("Запрос возвращает 403_Forbidden, пользователь не зарегистрирован")
    public void testCreatedUserNoEmail() {
        user = new UserModel(null, PASSWORD, NAME);
        response = createUser(user);

        boolean actual = response
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .path("success");

        Assert.assertFalse("Создан пользователь без Email", actual);
    }

    @Test
    @DisplayName("Создание пользователя, не заполняя поле Password")
    @Description("Запрос возвращает 403_Forbidden, пользователь не зарегистрирован")
    public void testCreatedUserNoPassword() {
        user = new UserModel(EMAIL, null, NAME);
        response = createUser(user);

        boolean actual = response
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .path("success");

        Assert.assertFalse("Создан пользователь без Password", actual);
    }

    @Test
    @DisplayName("Создание пользователя, не заполняя поле Name")
    @Description("Запрос возвращает 403_Forbidden, пользователь не зарегистрирован")
    public void testCreatedUserNoName() {
        user = new UserModel(EMAIL, PASSWORD, null);
        response = createUser(user);

        boolean actual = response
                .then()
                .log().all()
                .statusCode(HTTP_FORBIDDEN)
                .extract()
                .path("success");

        Assert.assertFalse("Создан пользователь без Name", actual);
    }

    @After
    public void cleanUp() {
        try {
            String token = accessToken(response);
            deleteUser(token);
        } catch (Exception e) {
            System.out.println("Ошибка при очистке, токен: " + e.getMessage());
        }
    }
}