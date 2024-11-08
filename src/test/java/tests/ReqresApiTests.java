package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import models.*;
import org.junit.jupiter.api.Test;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
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

        LoginResponseModel response = step("Отправляем POST запрос на логин", ()->
         given()
                .filter(withCustomTemplates())
                .body(authData)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .log().headers()
        .when()
                .post("https://reqres.in/api/login")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(LoginResponseModel.class));

        step("Проверяем полученный токен при успешном логине", ()->
        assertEquals("QpwL5tke4Pnpja7X4",response.getToken()));
       
    }

    @Test
    void createUserTest() {

        UserDataBodyModel userData = new UserDataBodyModel();
        userData.setName("morpheus");
        userData.setJob("leader");

      UserDataResponseModel response = step("Отправляем POST запрос добавление юзера", ()-> given()
                .filter(withCustomTemplates())
                .body(userData)
                .contentType(JSON)
                .log().uri()
                .log().body()
                .log().headers()
        .when()
                .post("https://reqres.in/api/users")
        .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().as(UserDataResponseModel.class));

        step("Проверяем имя и должность созданного юзера", ()->{
        assertEquals("morpheus",response.getName());
        assertEquals("leader",response.getJob());});
    }

    @Test
    void getUsersTest() {

        UserDataMainResponseModel response = step("Отправляем GET запрос на получение юзеров со 2-ой страницы", ()->
                given()
                .filter(withCustomTemplates())
                .log().uri()
        .when()
                .get("https://reqres.in/api/users?page=2")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(UserDataMainResponseModel.class));

        step("Проверяем имя полученного юзера", ()->
        assertEquals("Michael",response.getData().get(0).getFirst_name()));

    }

    @Test
    void getSingleUserTest() {

       UserSingleResponseModel response = step("Отправляем GET запрос на получение 2-го юзера", ()->
                 given()
                .filter(withCustomTemplates())
                .log().uri()
        .when()
                .get("https://reqres.in/api/users/2")
        .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(UserSingleResponseModel.class));

        step("Проверяем данные полученного юзера", ()->{
        assertEquals(2,response.getData().getId());
        assertEquals("janet.weaver@reqres.in",response.getData().getEmail());
        assertEquals("Janet",response.getData().getFirst_name());
        assertEquals("Weaver",response.getData().getLast_name());});

    }

    @Test
    void checkUserNotFoundTest() {

        step("Отправляем GET запрос на получение 13-го юзера", ()->
        given()
                .filter(withCustomTemplates())
                .log().uri()
        .when()
                .get("https://reqres.in/api/users/13")
        .then()
                .log().status()
                .log().body()
                .statusCode(404));
    }

}
