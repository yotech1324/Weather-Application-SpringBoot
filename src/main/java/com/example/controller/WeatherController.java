package com.example.controller;

import com.example.models.Weather;
import com.example.services.WeatherServices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {


    private static final Logger logger = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private WeatherServices weatherServices;

    // Fetch weather by country (PathVariable)
    @GetMapping("/find-by-country/{country}")
    public ResponseEntity<Weather> getWeatherByCountry(@PathVariable String country) {
        logger.info("Fetching weather data for country: {}", country);

        try {
            Weather weather = weatherServices.getWeatherData(country);

            if (weather != null) {
                logger.info("Weather data retrieved for country: {}", country);
                return ResponseEntity.ok(weather);
            } else {
                logger.warn("Weather data not found for country: {}", country);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error fetching weather data for country: {}. Exception: {}", country, e.getMessage());
            return ResponseEntity.internalServerError().build();
        } // throws call jaha se huya hai
    }

    // Add new weather data (POST request)
    @PostMapping("/add")
    public ResponseEntity<Weather> addWeather(@RequestBody Weather weather) {
        logger.info("Adding weather data for country: {}", weather.getCountry());

        try {
            Weather savedWeather = weatherServices.saveWeather(weather);
            logger.info("Weather data added successfully for country: {}", weather.getCountry());
            return ResponseEntity.ok(savedWeather);
        } catch (Exception e) {
            logger.error("Failed to add weather data for country: {}. Exception: {}", weather.getCountry(), e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    // Update weather data by country (PUT request)
    @PutMapping("/update/{country}")
    public ResponseEntity<Weather> updateWeather(@PathVariable String country, @RequestBody Weather weatherDetails) {
        logger.info("Updating weather data for country: {}", country);

        Optional<Weather> updatedWeather = weatherServices.updateWeatherByCountry(country, weatherDetails);

        if (updatedWeather.isPresent()) {
            logger.info("Weather data updated for country: {}", country);
            return ResponseEntity.ok(updatedWeather.get());
        } else {
            logger.warn("Weather data not found for update for country: {}", country);
            return ResponseEntity.notFound().build();
        }
    }

    // Delete weather data by country (DELETE request)
    @DeleteMapping("/delete/{country}")
    public ResponseEntity<String> deleteWeather(@PathVariable String country) {
        logger.info("Request to delete weather data for country: {}", country);

        boolean isDeleted = weatherServices.deleteWeatherByCountry(country);

        if (isDeleted) {
            logger.info("Weather data deleted for country: {}", country);
            return ResponseEntity.ok("Weather data deleted successfully.");
        } else {
            logger.warn("No weather data found to delete for country: {}", country);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/export/weather")
    public ResponseEntity<String> exportWeatherData(@RequestParam String filePath) {
        try {

            String exportPath = filePath.replace("\\", "/");

            weatherServices.exportWeatherDataToExcel(filePath);
            return ResponseEntity.ok("Excel file created at: " + filePath);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Failed to export: " + e.getMessage());
        }
    }
}

//http://localhost:8080/api/weather/export/weather/C:/Downloads
