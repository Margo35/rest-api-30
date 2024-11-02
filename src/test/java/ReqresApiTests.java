import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresApiTests {

    @Test
    void successfulLoginTest() {
        String authData = "{\"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\"}";
        given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void createUserTest() {
        String userData = "{\"name\": \"morpheus\", \"job\": \"leader\"}";
        given()
                .body(userData)
                .contentType(JSON)
                .log().uri()
        .when()
                .post("https://reqres.in/api/users")
        .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .body("name", is("morpheus"))
                .body("job", is("leader"));
    }

    @Test
    void getUsersTest() {

        given()
                .log().uri()
        .when()
                .get("https://reqres.in/api/users?page=2")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("total", is(12));
    }

    @Test
    void getSingleUserTest() {

        given()
                .log().uri()
        .when()
                .get("https://reqres.in/api/users/2")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("data.id", is(2))
                .body("data.email", is("janet.weaver@reqres.in"));

    }

    @Test
    void checkUserNotFoundTest() {

        given()
                .log().uri()
        .when()
                .get("https://reqres.in/api/users/13")
        .then()
                .log().status()
                .log().body()
                .statusCode(404);
    }

}
