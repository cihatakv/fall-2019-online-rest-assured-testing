package com.automation.tests.day3;

import io.restassured.http.ContentType;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class ORDSTestsDay3 {

    @BeforeAll
    public static void setup(){
        baseURI = "http://54.224.118.38:1000/ords/hr";
    }

    /**
     * given path parameter is "/regions/{id}"
     * when user makes get request
     * and region id is equals to 1
     * then assert that status code is 200
     * and assert that region name is Europe
     */

    @Test
    public void verifyFirstRegion(){

        given().
                pathParam("id", 1).
        when().
                get("/regions/{id}").prettyPeek().
        then().assertThat().
                statusCode(200).
                body("region_name", is("Europe")).
                body("region_id", is(1)).
                time(lessThan(5L), TimeUnit.SECONDS);

    }

    @Test
    public void verifyEmployee(){
        Response response = given().
                                    accept(ContentType.JSON).
                            when().
                                    get("/employees");


        JsonPath jsonPath = response.jsonPath();

        String nameOfFirstEmployee = jsonPath.getString("items[0].first_name"); // 0 to get first item
        String nameOfLastEmployee = jsonPath.getString("items[-1].first_name"); // -1 to get last item
        String nameOfLastSecondEmployee = jsonPath.getString("items[-2].first_name"); // -1 to get last second item


        System.out.println("First name of 1st employee: " + nameOfFirstEmployee);
        System.out.println("First name of last employee: " + nameOfLastEmployee);
        System.out.println("First name of last second employee: " + nameOfLastSecondEmployee);


        Map<String, ?> firstEmployee = jsonPath.get("items[0]");

        System.out.println(firstEmployee);



    }

}
