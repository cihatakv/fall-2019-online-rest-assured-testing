package com.automation.tests.day2;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class SpartanTests {

    //URI (Uniform Resource Identifier) = URL + URN = http://www.google.com/index.html
    //URL (Uniform Resource Locator)    = http://www.google.com
    //URN (Uniform Resource Name)       = /index.html

    String BASE_URL = "http://3.90.112.152:8000";

    @Test
    @DisplayName("Get list of all spartans")
    public void getAllSpartans() {

        //401 - unauthorized
        //how to provide credentials?
        //there is difference tyoes of authentication: basic, oath, oath 2.0, api key etc...
        //spartan app requires basic authentication: username and password

        given().
                auth().basic("admin", "admin").
                baseUri(BASE_URL).
        when().
                get("/api/spartans").prettyPeek().
        then().statusCode(200);
    }

    // add new spartan

    @Test
    @DisplayName("Add new spartan")
    public void addSpartan(){
        //JSON supports different data types: string, integer, boolean
        String body = "{\"gender\": \"Male\", \"name\": \"Random User\", \"phone\": 9999999999}";
        //instead of string variable, we can use external JSON file
        //use File class to read JSON and pass it into body
        //provide path to the JSON as a parameter
        File jsonFile = new File(System.getProperty("user.dir") + "/spartan.json");

        given().
                contentType(ContentType.JSON).
                auth().basic("admin", "admin").
                body(jsonFile).
                baseUri(BASE_URL).
        when().
                post("/api/spartans").prettyPeek().
        then().
                statusCode(201);
    }

    @Test
    @DisplayName("Delete some spartan and verify that status code is 204")
    public void deleteSpartanTest(){
        //{id}
        given().
                auth().basic("admin", "admin").
                baseUri(BASE_URL).
        when().
                delete("/api/spartans/{id}", 792).prettyPeek().
        then().
                statusCode(204);
    }

}
