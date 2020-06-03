package com.automation.office_hours;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.*;

import java.util.*;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import static org.hamcrest.MatcherAssert.assertThat;


public class ORDSTestCases {

    @BeforeAll
    public static void setup(){
        RestAssured.baseURI = "http://54.146.89.247:1000/ords/hr/";
    }

    /*
    get all the records from the employees table using the }/employees
    verify that number of employees is more than 100
     */

    // https://jsonpath.com/ in order to evaluate json file
    @Test
    public void employeesTest(){

        Response response = given().
//                baseUri(baseURI).
        queryParam("limit", 1000).
                                    get("/employees");

        response.then().statusCode(200);

        List<Map<String, Object>> employees = response.jsonPath().getList("items");
        System.out.println("employees.size() = " + employees.size());
        System.out.println("employees.get(0) = " + employees.get(0));
        System.out.println("employees.get(1) = " + employees.get(1));
        assertThat(employees.size(),  greaterThan(100));

    }


    /*
    get all the employees and their depart ids.
    verify that department id points to the existing record in the departmetns table
    verify response 200
    verify department name is not empty
     */

    @Test
    public void verifyDepartmentName() {
//        List<Integer> depIds = given().
////                baseUri(baseURI).
//        queryParam("limit", 1000).
//                        get("/employees").jsonPath().getList("items.department_id");
//
//        System.out.println("depIds = " + depIds);

        List<Integer> depIds = given().
                queryParam("limit", 110).
                when().
                get("/employees").jsonPath().getList("items.department_id");
        System.out.println(depIds);
        // remove duplicates
        // Set<Integer>uniqueDepIds=new HashSet<>(depIds)

        Set<Integer> uniqueDepIds = new HashSet<>(depIds);
        System.out.println(uniqueDepIds);

        // get each separately
        for (Integer depId : uniqueDepIds) {
            // call the department/:id to get the specific department
            // verify 200, verify name is not null
            given().
                    pathParam("id", depId).
                    when().
                    get("/departments/{id}").
                    prettyPeek().
                    then().statusCode(200).and().body("department_name", not(emptyOrNullString()));
        }
    }
}
