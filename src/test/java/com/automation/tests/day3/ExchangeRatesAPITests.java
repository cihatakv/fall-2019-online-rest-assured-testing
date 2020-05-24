package com.automation.tests.day3;

import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class ExchangeRatesAPITests {

    @BeforeAll
    public static void setup(){
        baseURI = "http://api.openrates.io";
    }

    @Test
    @DisplayName("Get latest Rates based on Default")
    public void getLatestRate() {
        Response response = get("/latest").prettyPeek();
        response.then().statusCode(200);
    }

    @Test
    @DisplayName("Get latest Rates based on Custom Currency")
    public void getLatestRateBasedOnCustom() {
        Response response = given().
                                    queryParam("base", "USD").
                            when().
                                    get("/latest").prettyPeek();

        Headers headers = response.getHeaders();

        String contentType = headers.getValue("Content-Type");
        System.out.println("contentType = " + contentType);

        response.then().statusCode(200);
        response.then().assertThat().body("base", is("USD"));

        // lets verify that respinse contains todays date
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        response.then().assertThat().body("date", containsString(date));
    }

    @Test
    @DisplayName("Get Historical Rates")
    public void getHistoryOfRates(){

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Response response = given().
                                    queryParam("base", "USD").
                            when().
                                    get("/2008-02-01").prettyPeek();

        Headers headers = response.getHeaders();
//        System.out.println(headers);

        response.then().statusCode(200);
        response.then().assertThat().
                                    statusCode(200).
                            and().
                                    body("date", is("2008-02-01")).
                            and().
                                    body("rates.EUR", is(0.67163676f));

        //and() doesn't have a functional role, it's just a syntax sugar
        //we can chain validations
        //how we can retrieve
        //rates - it's an object
        //all currencies are like instance variables
        //to get any instance variable (property), objectName.propertyName

        Float param = response.jsonPath().get("rates.EUR");

        System.out.println(param);
    }

}
