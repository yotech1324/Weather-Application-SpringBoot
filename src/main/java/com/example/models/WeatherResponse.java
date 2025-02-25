// yeh api se jo response aa rha hai uske liye  kyunki api se aana vala data complex hai nested hai 
// Nested classes (Main, WeatherDetails, Sys) match the exact structure of the JSON.
package com.example.models;

import java.util.List;

public class WeatherResponse {
    
    private Main main;
    private List<WeatherDescription> weather;
    private Sys sys;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<WeatherDescription> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherDescription> weather) {
        this.weather = weather;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    // Inner Classes to match API structure
    public static class Main {
        private double temp;
        private double pressure;
        private double humidity;

        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
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
    }

    public static class WeatherDescription {
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static class Sys {
        private String country;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }
}
