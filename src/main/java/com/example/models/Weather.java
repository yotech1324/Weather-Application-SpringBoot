//Database ka weather manage krne ke liye 

package com.example.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Document(collection = "weather")  // This specifies the MongoDB collection
@ToString
@Getter
@Setter
public class Weather {

    private String id;  // Unique identifier for MongoDB
    /*@Indexed(unique = true)*/

    private String country;
    private double pressure;
    private double humidity;
    private double temp;
    private String description;
    

    // Constructors
    public Weather() {}

    public Weather(String country, double pressure, double humidity, double temp, String description, String source) {
        this.country = country;
        this.pressure = pressure;
        this.humidity = humidity;
        this.temp = temp;
        this.description = description;
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
