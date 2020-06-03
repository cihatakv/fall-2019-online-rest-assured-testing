package com.automation.office_hours;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class APIKeyExample {
    // MkvKGI2QQD19MI0q24VZP6qcguGV95pl8gA7f7vZqrHjp7X25gDYbDq4lzWSei5R
    @BeforeAll
    public static void setUp(){
        RestAssured.baseURI = "https://www.zipcodeapi.com/";
    }

    @Test
    public void testDistance(){
        given().
                pathParam("api_key", "MkvKGI2QQD19MI0q24VZP6qcguGV95pl8gA7f7vZqrHjp7X25gDYbDq4lzWSei5R").
                pathParam("zip_code1", "20005").
                pathParam("zip_code2", "20001").
                pathParam("units", "miles").
        when().
                get("/rest/{api_key}/distance.json/{zip_code1}/{zip_code2}/{units}").
                prettyPeek().
        then().
                statusCode(200).
                body("distance", not(emptyOrNullString()));
    }

    @Test
    public void testMB(){
        given().
                pathParam("api_key", "MkvKGI2QQD19MI0q24VZP6qcguGV95pl8gA7f7vZqrHjp7X25gDYbDq4lzWSei5R").
                pathParam("zip_code", "29577").
                pathParam("units", "degrees").
        when().
                get("/rest/{api_key}/info.json/{zip_code}/{units}").
                prettyPeek().
        then().
                assertThat().statusCode(is(200)). // hamcrest matcher method
        and().
                assertThat().body("city", is("Myrtle Beach"));
    }
}
