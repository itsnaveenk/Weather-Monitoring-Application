package com.example.weather_monitoring.service;

import com.example.weather_monitoring.Utils.Utils;
import com.example.weather_monitoring.model.WeatherData;
import com.example.weather_monitoring.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {
    @Autowired
    private WeatherDataRepository dataRepo;

    @Autowired
    private EmailAlertService emailService;

    public void checkTemperatureThreshold(String city, Double thresholdCelsius) {
        List<WeatherData> lastTwoReadings = dataRepo.findTop2ByCityOrderByTimestampDesc(city);

        if (lastTwoReadings.size() >= 2 && isThresholdBreached(lastTwoReadings, thresholdCelsius)) {
            triggerAlert(city, thresholdCelsius, lastTwoReadings);
        }
    }

    private boolean isThresholdBreached(List<WeatherData> readings, Double threshold) {
        return readings.stream()
                .allMatch(d ->
                        Utils.kelvinToCelsius(d.getTemperatureKelvin()) > threshold);
    }

    private void triggerAlert(String city, Double threshold, List<WeatherData> readings) {
        // Console Alert
        System.out.printf("ALERT: Temperature in %s exceeded %.1f°C%n", city, threshold);

        // Email Alert
        emailService.sendAlertEmail(city, threshold, readings);
    }
}