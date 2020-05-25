package com.automation.review;

import io.restassured.response.Response;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;

public class WeatherAPP {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter city name: ");
        String city = scanner.nextLine();
        String woeid = getWOEID(city);
        printWeatherInfo(woeid);
    }

    static {
        baseURI = "https://www.metaweather.com/api/location";
    }

    public static String getWOEID(String city) {
        //        Response response = get("/search/?query=" + city).prettyPeek();
        Response response = given().queryParam("query", city).get("/search");
        String woeid = response.jsonPath().getString("woeid");
        System.out.println("WOIED :: " + woeid);
        return woeid;
    }

    public static void printWeatherInfo(String woeid){
        woeid = woeid.replaceAll("\\D", ""); // to delete all non-digits
        Response response = get("{woeid}", woeid).prettyPeek();
        List<String> weatherStateName = response.jsonPath().getList("consolidated_weather.weather_state_name");
        List<String> temp = response.jsonPath().getList("consolidated_weather.the_temp");
        List<String> dates = response.jsonPath().getList("consolidated_weather.applicable_date");
//        System.out.println(weatherStateName);
//        System.out.println(temp);
//        System.out.println(dates);
        System.out.println("Here is weather forecast for this week");
        for (int i = 0; i < weatherStateName.size(); i++) {
            String date = dates.get(i);
            date = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyy-MM-dd")).format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
            System.out.printf("Date :: %-10s Weather state :: %-11s Temperature :: %-6s\n", date, weatherStateName.get(i), temp.get(i));
        }
    }

}
