
 package com.example.services;
import com.example.dao.WeatherRepository;
import com.example.models.Weather;
import com.example.models.WeatherResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import java.io.File;
import java.io.FileOutputStream;

@Service  // service chahiye taki map kr sake controller ke sath  doubt hai
public class WeatherServices {

    @Autowired
    private WeatherRepository weatherRepository;


    @Autowired
    private RestTemplate restTemplate;

    private final String apiKey = "f88bd2a7ccf69566e55449623bfd6f9e";

    private static final Logger logger = LoggerFactory.getLogger(WeatherServices.class);



    public Weather getWeatherData(String country) {
        // 1. Check if weather data exists in DB

       Optional<Weather> existingWeather = weatherRepository.findByCountry(country);


        if (existingWeather.isPresent() )  // is Empty dala kyunku index 0 of length 0 de rha tha
       {
        	logger.info("Fetching from Database");
            return existingWeather.get();
        }

        // 2. If not found, fetch from OpenWeather API
       else {
    	  logger.info("Fetching From api");
        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + country + "&appid=" + apiKey + "&units=metric";
        WeatherResponse apiResponse = restTemplate.getForObject(url, WeatherResponse.class); // API response ko java class ke sath map karega

        // 3. Map API response to Weather entity  After receiving the API response, the service convert the WeatherResponse object into a Weather object for storing in the database.
        Weather weather = new Weather();
        if (apiResponse != null && apiResponse.getMain() != null) {
            weather.setTemp(apiResponse.getMain().getTemp());
            weather.setPressure(apiResponse.getMain().getPressure());
            weather.setHumidity(apiResponse.getMain().getHumidity());
        }

        if (apiResponse != null && apiResponse.getSys() != null) {
            weather.setCountry(apiResponse.getSys().getCountry());
        } else {
            weather.setCountry(country);  // Default to requested country
        }

        if (apiResponse != null && apiResponse.getWeather() != null && !apiResponse.getWeather().isEmpty()) {
            weather.setDescription(apiResponse.getWeather().get(0).getDescription());
        } else {
            weather.setDescription("N/A");
        }

        // 4. Save to DB and return
        return weatherRepository.save(weather);
    }
    }


    public boolean deleteWeatherByCountry(String country) {
        Optional<Weather> existingWeather = weatherRepository.findByCountry(country);
        if (existingWeather.isPresent() ) {
            weatherRepository.delete(existingWeather.get());  // Delete first found entry
            return true;
        }
        return false;
    }

    public Weather saveWeather(Weather weather) {
        return weatherRepository.save(weather);
    }

    public Optional<Weather> updateWeatherByCountry(String country, Weather weatherDetails) {
        Optional<Weather> existingWeather = weatherRepository.findByCountry(country);
        if (existingWeather.isPresent()) {
            Weather weather = existingWeather.get();  // Update first found entry
            weather.setTemp(weatherDetails.getTemp());
            weather.setPressure(weatherDetails.getPressure());
            weather.setHumidity(weatherDetails.getHumidity());
            weather.setDescription(weatherDetails.getDescription());
            return Optional.of(weatherRepository.save(weather));
        }
        return Optional.empty();
    }

    public void exportWeatherDataToExcel(String filePath) throws IOException {

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        List<Weather> weatherList = weatherRepository.findAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Weather Data");

        // Header Row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Country", "Temperature", "Pressure", "Humidity", "Description"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Data Rows
        int rowNum = 1;
        for (Weather weather : weatherList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(weather.getCountry());
            row.createCell(1).setCellValue(weather.getTemp());
            row.createCell(2).setCellValue(weather.getPressure());
            row.createCell(3).setCellValue(weather.getHumidity());
            row.createCell(4).setCellValue(weather.getDescription());
        }

        // Autosize columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to Excel file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }
        workbook.close();
        System.out.println("Weather data exported successfully to " + filePath);
    }

}