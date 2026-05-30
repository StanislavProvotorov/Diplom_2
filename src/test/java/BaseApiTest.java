import io.restassured.RestAssured;
import org.junit.BeforeClass;

import static data.UserData.BASE_URL;

public class BaseApiTest {
    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
    }
}