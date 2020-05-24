package com.automation.tests.day2;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class MetaWeatherTest {

    String BASE_URI = "https://www.metaweather.com/api";

    @Test
    @DisplayName("sth")
    public void getWeather(){

        Response response = given().baseUri(BASE_URI).when().get("/location/search/?query=houston");
        JsonPath jsonPath = response.jsonPath();

        String woeid = jsonPath.getString("woeid");
        given().baseUri(BASE_URI).when().get("/location/" + woeid.substring(1, woeid.indexOf(']'))).prettyPeek().then().statusCode(200);

    }

}
