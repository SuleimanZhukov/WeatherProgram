package com.suleiman.weather.model;

import com.suleiman.weather.model.entity.*;

public class WeatherRequest {
    private Main main;
    private Weather[] weather;
    private String name;

    public Weather[] getWeather() {
        return weather;
    }

    public String getName() {
        return name;
    }

    public Main getMain() {
        return main;
    }
}
