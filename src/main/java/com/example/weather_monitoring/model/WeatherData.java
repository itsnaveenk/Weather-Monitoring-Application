package com.example.weather_monitoring.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(indexes = @Index(
        name = "idx_city_timestamp",
        columnList = "city, timestamp"
))
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private Double temperatureKelvin;
    private Double feelsLikeKelvin;
    private String weatherCondition;
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime timestamp;
    private Double humidity;
    private Double windSpeed;
    private Double pressure;
}