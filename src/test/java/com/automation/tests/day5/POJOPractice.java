package com.automation.tests.day5;

import com.automation.pojos.Spartan;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import com.automation.utilities.ConfigurationReader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
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

public class POJOPractice {

    @BeforeAll
    public static void beforeAll(){
        baseURI = ConfigurationReader.getProperty("SPARTAN.URI");
    }

    @Test
    public void getUser(){
        Response response = given().
                                    auth().
                                    basic("admin", "admin").
                            when().
                                    get("/spartans/{id}", 393).prettyPeek();
        Spartan spartan = response.as(Spartan.class);
        System.out.println(spartan);

        assertEquals(393, spartan.getId());
        assertEquals("Michael Scott", spartan.getName());
        assertEquals("Male", spartan.getGender());


        Map<String, ?> spartanAsMap = response.as(Map.class);
        System.out.println(spartanAsMap);

    }

    @Test
    public void addUser(){
        Spartan spartan = new Spartan("Hasan Jan", "Male", 31231241121L);

        Gson gson = new Gson();
        String pojoAsJson = gson.toJson(spartan);
        System.out.println(pojoAsJson);

        Response response = given().
                                    auth().
                                    basic("admin", "admin").
                                    contentType(ContentType.JSON).
                                    body(spartan).
                            when().
                                    post("/spartans").prettyPeek();

        response.then().statusCode(201); // to ensure that user was created

        int userId = response.jsonPath().getInt("data.id");

        System.out.println("User's id :: " + userId);

        System.out.println("####DELETE USER####");

        given(). // just for setup auth, cookies, pathparameters
                auth().
                basic("admin", "admin").
        when(). // specify request
                delete("/spartans/{id}", userId).prettyPeek().
        then().
                assertThat().statusCode(204); // to ensure that user was deleted.




    }


}
