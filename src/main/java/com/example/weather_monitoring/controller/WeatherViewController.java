package com.example.weather_monitoring.controller;

import com.example.weather_monitoring.repository.DailySummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WeatherViewController {

    @Autowired
    private DailySummaryRepository summaryRepo;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("summaries", summaryRepo.findAll());
        return "dashboard";
    }
}