package fi.tuni.softwaredesign.eventapp.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class for storing weather information
 */
public class Weather {
    private final double temperature;
    private final double feelsLikeTemperature;
    private final double rain;
    private final double windSpeed;
    private final String mainDescription;
    private final String description;
    private final LocalDateTime dateTime;
    
    /**
     * Constructor for Weather
     * @param temperature temperature in celsius
     * @param feelsLikeTemperature feels like temperature in celsius
     * @param rain rain amount in mm
     * @param windSpeed wind speed in m/s
     * @param mainDescription main description
     * @param description description
     * @param dateTime date and time
     */
    public Weather(double temperature, double feelsLikeTemperature, double rain, double windSpeed, String mainDescription, String description, LocalDateTime dateTime) {
        this.temperature = temperature;
        this.feelsLikeTemperature = feelsLikeTemperature;
        this.rain = rain;
        this.windSpeed = windSpeed;
        this.mainDescription = mainDescription;
        this.description = description;
        this.dateTime = dateTime;
    }
    
    /**
     * Returns the temperature
     * @return temperature
     */
    public double getTemperature() {
        return this.temperature;
    }
    
    /**
     * Returns the feels like temperature
     * @return feelsLikeTemperature
     */
    public double getFeelsLikeTemperature() {
        return this.feelsLikeTemperature;
    }
    
    /**
     * Returns the rain amount
     * @return rain
     */
    public double getRain() {
        return this.rain;
    }
    
    /**
     * Returns the wind speed
     * @return windSpeed
     */
    public double getWindSpeed() {
        return this.windSpeed;
    }
    
    /**
     * Returns the main description
     * @return mainDescription
     */
    public String getMainDescription() {
        return this.mainDescription;
    }
    
    /**
     * Returns the description
     * @return description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the date and time
     * @return dateTime
     */
    public LocalDateTime getDateTime() {
        return this.dateTime;
    }

    /**
     * Returns the date as a string
     * @return date as a string
     */
    public String getDateString() {
        return this.dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
