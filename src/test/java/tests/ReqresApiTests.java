package tests;

import models.*;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReqresApiTests {

    @Test
    void successfulLoginTest() {

        LoginBodyModel authData = new LoginBodyModel();
        authData.setEmail("eve.holt@reqres.in");
        authData.setPassword("cityslicka");

       LoginResponseModel response = given()
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .log().body()
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseModel.class);

       assertEquals("QpwL5tke4Pnpja7X4",response.getToken());
       
    }

    @Test
    void createUserTest() {

        UserDataBodyModel userData = new UserDataBodyModel();
        userData.setName("morpheus");
        userData.setJob("leader");

      UserDataResponseModel response = given()
                .body(userData)
                .contentType(JSON)
                .log().uri()
        .when()
                .post("https://reqres.in/api/users")
        .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().as(UserDataResponseModel.class);

        assertEquals("morpheus",response.getName());
        assertEquals("leader",response.getJob());
    }

    @Test
    void getUsersTest() {

        UserDataMainResponseModel response = given()
                .log().uri()
        .when()
                .get("https://reqres.in/api/users?page=2")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(UserDataMainResponseModel.class);

        assertEquals("Michael",response.getData().get(0).getFirst_name());

    }

    @Test
    void getSingleUserTest() {

       UserSingleResponseModel response = given()
                .log().uri()
        .when()
                .get("https://reqres.in/api/users/2")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(UserSingleResponseModel.class);

        assertEquals(2,response.getData().getId());
        assertEquals("janet.weaver@reqres.in",response.getData().getEmail());
        assertEquals("Janet",response.getData().getFirst_name());
        assertEquals("Weaver",response.getData().getLast_name());

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
