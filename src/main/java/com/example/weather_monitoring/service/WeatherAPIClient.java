package com.example.weather_monitoring.service;

import com.example.weather_monitoring.model.WeatherData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class WeatherAPIClient {
    @Value("${openweathermap.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q={city}&appid={key}";

    public WeatherData fetchWeatherData(String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = API_URL.replace("{city}", city).replace("{key}", apiKey);

            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> mainData = (Map<String, Object>) response.getBody().get("main");
            Map<String, Object> weather = (Map<String, Object>) ((List) response.getBody().get("weather")).get(0);
            Map<String, Object> wind = (Map<String, Object>) response.getBody().get("wind");

            WeatherData data = new WeatherData();
            data.setCity(city);
            data.setTemperatureKelvin(((Number) mainData.get("temp")).doubleValue());
            data.setFeelsLikeKelvin(((Number) mainData.get("feels_like")).doubleValue());
            data.setWeatherCondition((String) weather.get("main"));
            data.setTimestamp(LocalDateTime.now());
            data.setHumidity(((Number) mainData.get("humidity")).doubleValue());
            data.setWindSpeed(((Number) wind.get("speed")).doubleValue());
            data.setPressure(((Number) mainData.get("pressure")).doubleValue());

            return data;
        } catch (RestClientException e) {
            log.error("Failed to fetch weather data for {}: {}", city, e.getMessage());
            throw new RuntimeException("API request failed");
        }
    }
}