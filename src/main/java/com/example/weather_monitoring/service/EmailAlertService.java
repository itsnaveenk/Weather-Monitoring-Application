package com.example.weather_monitoring.service;

import com.example.weather_monitoring.Utils.Utils;
import com.example.weather_monitoring.model.WeatherData;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailAlertService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${weather.alerts.email}")
    private String alertEmail;

    @Async
    public void sendAlertEmail(String city, Double threshold, List<WeatherData> readings) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(alertEmail);
            helper.setSubject("Weather Alert: " + city);

            String content = buildEmailContent(city, threshold, readings);
            helper.setText(content, true); // true = HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email alert", e);
        }
    }

    private String buildEmailContent(String city, Double threshold, List<WeatherData> readings) {
        return """
            <html>
                <body>
                    <h2 style='color:red'>Weather Alert for %s!</h2>
                    <p>Temperature exceeded %.1f°C in two consecutive readings:</p>
                    <table border='1'>
                        <tr>
                            <th>Time</th>
                            <th>Temperature (°C)</th>
                        </tr>
                        %s
                    </table>
                </body>
            </html>
            """.formatted(city, threshold, buildTableRows(readings));
    }

    private String buildTableRows(List<WeatherData> readings) {
        return readings.stream()
                .map(r -> """
                <tr>
                    <td>%s</td>
                    <td>%.1f</td>
                </tr>
                """.formatted(r.getTimestamp(), Utils.kelvinToCelsius(r.getTemperatureKelvin())))
                .collect(Collectors.joining());
    }
}