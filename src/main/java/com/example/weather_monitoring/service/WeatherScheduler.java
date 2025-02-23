package com.example.weather_monitoring.service;

import com.example.weather_monitoring.model.WeatherData;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WeatherScheduler {
    @Autowired
    private WeatherAPIClient apiClient;
    @Autowired
    private WeatherService weatherService;

    private final List<String> CITIES = List.of("Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad");

    @Scheduled(fixedRateString = "${weather.fetch.interval:60000}") // 5 mins
    public void fetchWeatherData() {
        log.info("Fetching weather data for cities: {}", CITIES);
        System.out.println("Fetching weather data for cities: " + CITIES);
        CITIES.forEach(city -> {
            WeatherData data = apiClient.fetchWeatherData(city);
            weatherService.processAndSave(data);
        });

        log.info("Weather data fetch complete");
    }
}