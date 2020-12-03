package com.suleiman.weather.city;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CityRequest {
    private List<String> citiesNames = new ArrayList<>();

    {
        try {
            Object obj = new JSONParser().parse(new FileReader("src/main/java/com/suleiman/weather/city/city.list.json"));
            JSONArray jsonArray = (JSONArray) obj;

            jsonArray.forEach(city -> parseName((JSONObject) city));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void parseName(JSONObject cityName) {
        String name = (String) cityName.get("name");
        citiesNames.add(name);
    }

    public List<String> getCitiesNames() {
        return citiesNames;
    }
}
