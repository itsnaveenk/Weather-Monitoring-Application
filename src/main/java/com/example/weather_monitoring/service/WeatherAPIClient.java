package com.example.weather_monitoring.service;

import com.example.weather_monitoring.model.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class WeatherAPIClient {
    @Value("${openweathermap.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q={city}&appid={key}";

    public WeatherData fetchWeatherData(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = API_URL.replace("{city}", city).replace("{key}", apiKey);

        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
        Map<String, Object> mainData = (Map<String, Object>) response.getBody().get("main");
        Map<String, Object> weather = (Map<String, Object>) ((List) response.getBody().get("weather")).get(0);

        WeatherData data = new WeatherData();
        data.setCity(city);
        data.setTemperatureKelvin((Double) mainData.get("temp"));
        data.setFeelsLikeKelvin((Double) mainData.get("feels_like"));
        data.setWeatherCondition((String) weather.get("main"));
        data.setTimestamp(LocalDateTime.now());

        return data;
    }
}