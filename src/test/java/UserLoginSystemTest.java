import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.UserModel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static data.UserData.*;
import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static steps.UserSteps.*;

public class UserLoginSystemTest extends BaseApiTest {
    private UserModel user;
    private UserModel userLogin;
    private Response response;
    private String token;

    @Before
    public void setUpUser() {
        user = new UserModel(EMAIL, PASSWORD, NAME);
        response = createUser(user);
        token = accessToken(response);
    }

    @Test
    @DisplayName("Успешная авторизация пользователя")
    @Description("Запрос возвращает 200_OK, пользователь авторизован")
    public void testUserLoginRegisteredSystem() {
        userLogin = new UserModel(EMAIL, PASSWORD);

        boolean actual = loginSystemUser(userLogin)
                .then()
                .log().all()
                .statusCode(HTTP_OK)
                .extract()
                .path("success");

        Assert.assertTrue("Вход пользователя не осуществлен", actual);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным логином")
    @Description("Запрос возвращает 401_UNAUTHORIZED, пользователь не авторизован")
    public void testUserIncorrectLoginRegisteredSystem() {
        userLogin = new UserModel("122" + EMAIL, PASSWORD);

        String actual = loginSystemUser(userLogin)
                .then()
                .log().all()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract()
                .path("message");

        Assert.assertEquals("Вход пользователя с неверным логином", "email or password are incorrect", actual);
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Запрос возвращает 401_UNAUTHORIZED, пользователь не авторизован")
    public void testUserIncorrectPasswordRegisteredSystem() {
        userLogin = new UserModel(EMAIL, PASSWORD + "155");

        String actual = loginSystemUser(userLogin)
                .then()
                .log().all()
                .statusCode(HTTP_UNAUTHORIZED)
                .extract()
                .path("message");

        Assert.assertEquals("Вход пользователя с неверным логином", "email or password are incorrect", actual);
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