package com.automation.tests.day4;

import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class WarmUp {

    @BeforeAll
    public static void setup(){
        baseURI = ConfigurationReader.getProperty("ORDS.URI"); // "http://54.224.118.38:1000/ords/hr";
    }

    /**
     * Warmup!
     * Given accept type is JSON
     * When users sends a GET request to “/employees”
     * Then status code is 200
     * And Content type is application/json
     * And response time is less than 3 seconds
     */
    @Test
    @DisplayName("Verify status code, content type and response time")
    public void employeesTest1(){

        Response response = given().
                                    accept(ContentType.JSON).
                            when().
                                    get("/employees").prettyPeek();

        response.then().statusCode(200);
        response.then().contentType("application/json");
        response.then().time(lessThan(3L), TimeUnit.SECONDS);
    }

    /**
     *
     Given accept type is JSON
     And parameters: q = {"country_id":"US"}
     When users sends a GET request to "/countries"
     Then status code is 200
     And Content type is application/json
     And country_name from payload is "United States of America"
     *
     */

    @Test
    @DisplayName("Verify country name, content type and status code for country")
    public void verifyCountriesTest1(){

        given().
                accept(ContentType.JSON).
                queryParam("q", "{\"country_id\":\"US\"}").
        when().
               get("/countries").prettyPeek().
        then().assertThat().
                            statusCode(200).
                            contentType(ContentType.JSON).
                            body("items[0].country_name", is("United States of America"));

        // SECOND REQUEST
        Response response = given().
                                    accept(ContentType.JSON).
                            when().
                                    get("countries").prettyPeek();


        String countryName = response.jsonPath().getString("items.find{it.country_id == 'US'}.country_name");
        Map<String, Object> countryUS = response.jsonPath().get("items.find{it.country_id == 'US'}");

        //findAll countrynames from region 2


        List<String> countryNames = response.jsonPath().get("items.findAll{it.region_id == 2}.country_name");

        System.out.println("Country Name = " + countryName);
        System.out.println("countryUS = " + countryUS);
        System.out.println("Country Names = " + countryNames);

        for (Map.Entry<String, Object> entry : countryUS.entrySet()) {
            System.out.printf("key = %s, value = %s\n", entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Object> entry : countryUS.entrySet()) {
            System.out.printf("%s = %s\n", entry.getKey(), entry.getValue());
        }
    }

    // let's find employee with highest salary. Use Gpath
    @Test
    @DisplayName("find employee with highest salary with GPath")
    public void getEmployeeWithHighestSalaryTest(){
        Response response = when().get("/employees").prettyPeek();

        Map<String, ?> bestEmployee = response.jsonPath().get("items.max{it.salary}");

        System.out.println("bestEmployee = " + bestEmployee);
    }

    // let's find employee with least salary. Use Gpath
    @Test
    @DisplayName("find employee with highest salary with GPath")
    public void getEmployeeWithLeastSalaryTest(){
        Response response = when().get("/employees").prettyPeek();

        Map<String, ?> poorGuy = response.jsonPath().get("items.min{it.salary}");

        System.out.println("Poor Guy = " + poorGuy);
    }

    // let's find the average salary. Use Gpath
    @Test
    @DisplayName("find employee with highest salary with GPath")
    public void getAverageSalaryTest(){
        Response response = when().get("/employees").prettyPeek();

        int companyPayroll = response.jsonPath().get("items.collect{it.salary}.sum()");

        System.out.println("Company Payroll = " + companyPayroll);

    }

    /**
     * given path parameter is “/employees”
     * when user makes get request
     * then assert that status code is 200
     * Then user verifies that every employee has positive salary
     *
     */

    @Test
    @DisplayName("Verify that every employee has positive salary")
    public void testSalary(){
        when().
                get("/employees").
        then().assertThat().
                            statusCode(200).
                            body("items.salary", everyItem(greaterThan(0))).
                            log().ifError();
    }

    /**
     * given path parameter is “/employees/{id}”
     * and path parameter is 101
     * when user makes get request
     * then assert that status code is 200
     * and verifies that phone number is 515-123-4568
     *
     */

    @Test
    @DisplayName("verifies that phone number is 515-123-4568")
    public void verifyPhoneNumber(){
        Response response = when().
                get("/employees/{id}", 101).prettyPeek();

        response.then().statusCode(200);

        String expected = "515-123-4568";
        String actual = response.jsonPath().getString("phone_number").replace(".", "-");


        assertEquals(200, response.statusCode());
        assertEquals(expected, actual);


    }



}
