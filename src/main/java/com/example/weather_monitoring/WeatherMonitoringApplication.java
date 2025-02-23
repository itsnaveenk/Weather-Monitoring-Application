package com.example.weather_monitoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync(proxyTargetClass = true)
@SpringBootApplication
public class WeatherMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherMonitoringApplication.class, args);
    }

}
