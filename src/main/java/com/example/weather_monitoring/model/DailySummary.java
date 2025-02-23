package com.example.weather_monitoring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class DailySummary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private LocalDate date;
    private Double avgTemperature;
    private Double maxTemperature;
    private Double minTemperature;
    private String dominantCondition;
}