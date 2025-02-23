package com.example.weather_monitoring.repository;

import com.example.weather_monitoring.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {

    List<WeatherData> findTop2ByCityOrderByTimestampDesc(String city);

    @Query("SELECT w FROM WeatherData w " +
            "WHERE w.city = :city " +
            "AND DATE(w.timestamp) = :date")
    List<WeatherData> findByCityAndDate(@Param("city") String city,
                                        @Param("date") LocalDate date);
}
