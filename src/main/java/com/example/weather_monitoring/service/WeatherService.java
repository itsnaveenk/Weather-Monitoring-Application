package com.example.weather_monitoring.service;

import com.example.weather_monitoring.Utils.Utils;
import com.example.weather_monitoring.model.DailySummary;
import com.example.weather_monitoring.model.WeatherData;
import com.example.weather_monitoring.repository.DailySummaryRepository;
import com.example.weather_monitoring.repository.WeatherDataRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    @Value("${weather.alerts.temperature-threshold}")
    private Double temperatureThreshold;

    @Autowired
    private WeatherDataRepository dataRepo;
    @Autowired
    private DailySummaryRepository summaryRepo;
    @Autowired
    private AlertService alertService;


    @Transactional
    public void processAndSave(WeatherData data) {
        // Save raw data
        dataRepo.save(data);

        // Compute daily aggregates
        LocalDate today = LocalDate.now();
        List<WeatherData> todayData = dataRepo.findByCityAndDate(data.getCity(), today);

        DoubleSummaryStatistics stats = todayData.stream()
                .mapToDouble(d -> Utils.kelvinToCelsius(d.getTemperatureKelvin()))
                .summaryStatistics();

        String dominantCondition = todayData.stream()
                .collect(Collectors.groupingBy(WeatherData::getWeatherCondition, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Clear");

        DailySummary summary = new DailySummary();
        summary.setCity(data.getCity());
        summary.setDate(today);
        summary.setAvgTemperature(stats.getAverage());
        summary.setMaxTemperature(stats.getMax());
        summary.setMinTemperature(stats.getMin());
        summary.setDominantCondition(dominantCondition);

        summaryRepo.save(summary);
        alertService.checkTemperatureThreshold(data.getCity(), temperatureThreshold);

    }
}