package fi.tuni.softwaredesign.eventapp.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import fi.tuni.softwaredesign.eventapp.model.Weather;
import fi.tuni.softwaredesign.eventapp.model.WeatherApi;
import fi.tuni.softwaredesign.eventapp.view.WeatherView;

/**
 * Handles weather-related logic and updates the WeatherView.
 */
public class WeatherController {

    private WeatherView weatherView;
    private WeatherApi api;
    private Weather currentWeather;
    private String apiKey = "6b2fec01c3b7fff4d349829932cb1c09";
    private LocalDate todayDate;
    private ArrayList<Weather> forecastWeather;
    private ArrayList<Weather> filteredForecastWeather;

    /**
     * Constructor for WeatherController. Initializes the weather data and
     * updates the view.
     *
     * @param weatherView The WeatherView instance to be controlled
     */
    public WeatherController(WeatherView weatherView) {
        this.weatherView = weatherView;
        this.api = new WeatherApi(apiKey);
        this.forecastWeather = new ArrayList<>();
        this.filteredForecastWeather = new ArrayList<>();
        this.todayDate = LocalDate.now();

        initializeButtons(); // Set up button actions

        try {
            // Get coordinates for Tampere
            double[] coordinates = api.lookUpLocation("Tampere");
            double latitude = coordinates[0];
            double longitude = coordinates[1];

            // Fetch and process current weather data
            this.currentWeather = processWeather(api.currentWeatherAt(latitude, longitude));

            // Fetch and process forecast weather data
            this.forecastWeather = processForecastWeather(api.getForecastWeather(latitude, longitude, 0));

            // Filter the forecast data for display
            filterWeatherForecast();

            updateWeather();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Updates the weather forecast for a selected date. Updates the "Current
     * Weather" to match the selected date and refreshes the WeatherView with
     * the forecast for the next 4 days starting from the day after the selected
     * date.
     *
     * @param selectedDate The date selected by the user
     * @param eventController The EventController
     */
    public void updateWeatherForSelectedDate(LocalDate selectedDate, EventController eventController) {
        LocalDate maxDate = LocalDate.now().plusDays(5);

        // Handle case where the selected date is beyond the available range
        if (selectedDate.isAfter(maxDate)) {
            weatherView.UpdateWeather(0, "No weather data available. Selected date is beyond the forecast range.");
            weatherView.updateForecastPaneWithMessage("No weather data available. Selected date is beyond the forecast range.");
            return;
        }

        Weather selectedWeather = null;
        // Update the current weather based on the selected date
        if (selectedDate.equals(todayDate)) {
            selectedWeather = getTodayWeather();
        } else {
            for (Weather weather : forecastWeather) {
                if (weather.getDateTime().toLocalDate().equals(selectedDate)) {
                    selectedWeather = weather;
                    break;
                }
            }
        }
        
        // If no weather data is found for the selected date
        if (selectedWeather == null) {
            weatherView.UpdateWeather(0, "No weather data available for the selected date.");
            weatherView.updateForecastPaneWithMessage("No weather data available for the selected date.");
            return;
        }

        // Update current weather display
        currentWeather = selectedWeather;
        weatherView.UpdateWeather((int) currentWeather.getTemperature(), currentWeather.getMainDescription());

        // Update the forecast pane to show the next 4 days from the day after the selected date
        filterWeatherForecastFromNextDay(selectedDate);
        if (filteredForecastWeather.isEmpty()) {
            weatherView.updateForecastPaneWithMessage("No weather data available for the forecast.");
        } else {
            weatherView.updateForecastPane(filteredForecastWeather);
        }

        // Notify EventController to update events for the selected date
        if (eventController != null) {
            eventController.updateEventsForSelectedDate(selectedDate, currentWeather);
        }
    }

    /**
     * Filters the forecast weather starting from the day after a specific date.
     * Selects 4 forecasts for the next 4 days at 15:00.
     *
     * @param startDate The date selected by the user
     */
    private void filterWeatherForecastFromNextDay(LocalDate startDate) {
        filteredForecastWeather.clear();
        LocalDate date = startDate.plusDays(1); // Start from the day after the selected date

        for (int i = 0; i < 4; i++) {
            for (Weather weather : forecastWeather) {
                if (weather.getDateTime().toLocalDate().equals(date) && weather.getDateTime().getHour() == 15) {
                    filteredForecastWeather.add(weather);
                    break;
                }
            }
            date = date.plusDays(1);
        }
    }

    /**
     * Normalizes the weather type to match icon filenames.
     *
     * @param mainDescription The main weather description from the API
     * @return A normalized weather type string
     */
    private String normalizeWeatherType(String mainDescription) {
        switch (mainDescription.toLowerCase()) {
            case "clear":
                return "Sunny";
            case "clouds":
                return "Cloudy";
            case "rain":
                return "Rain";
            case "drizzle":
                return "Drizzle";
            case "snow":
                return "Snow";
            case "thunderstorm":
                return "Thunder";
            case "mist":
            case "fog":
                return "Cloudy";
            default:
                return "PartlyCloudy";
        }
    }

    /**
     * Processes a Weather object by normalizing its weather type.
     *
     * @param weather The Weather object to process
     * @return A new Weather object with the normalized weather type
     */
    private Weather processWeather(Weather weather) {
        String normalizedType = normalizeWeatherType(weather.getMainDescription());
        return new Weather(
                weather.getTemperature(),
                weather.getFeelsLikeTemperature(),
                weather.getRain(),
                weather.getWindSpeed(),
                normalizedType,
                weather.getDescription(),
                weather.getDateTime()
        );
    }

    /**
     * Processes a list of forecast Weather objects. Filters forecasts for
     * specific times (e.g., 15:00) for better granularity.
     *
     * @param forecasts The list of forecast Weather objects
     * @return A list of processed Weather objects
     */
    private ArrayList<Weather> processForecastWeather(ArrayList<Weather> forecasts) {
        ArrayList<Weather> processedForecasts = new ArrayList<>();

        for (Weather weather : forecasts) {
            if (weather.getDateTime().getHour() == 15) {
                processedForecasts.add(processWeather(weather));
            }
        }
        return processedForecasts;
    }

    /**
     * Initializes the navigation buttons for the DatePicker. Updates the
     * DatePicker value when navigation buttons are clicked.
     */
    private void initializeButtons() {
        weatherView.getLeftButton().setOnAction(e -> {
            LocalDate currentDate = weatherView.getDatePicker().getValue();
            if (currentDate != null) {
                weatherView.getDatePicker().setValue(currentDate.minusDays(1));
            }
        });
        weatherView.getRightButton().setOnAction(e -> {
            LocalDate currentDate = weatherView.getDatePicker().getValue();
            if (currentDate != null) {
                weatherView.getDatePicker().setValue(currentDate.plusDays(1));
            }
        });
    }

    /**
     * Filters the forecast weather data to display the next four days. Filters
     * forecasts specifically for 15:00.
     */
    public void filterWeatherForecast() {
        filteredForecastWeather.clear();
        LocalDate date = LocalDate.now().plusDays(1);

        for (int i = 0; i < 4; i++) {
            for (Weather weather : forecastWeather) {
                if (weather.getDateTime().toLocalDate().equals(date) && weather.getDateTime().getHour() == 15) {
                    filteredForecastWeather.add(weather);
                    break;
                }
            }
            date = date.plusDays(1);
        }
    }

    /**
     * Updates the WeatherView with the current weather and forecast data.
     * Displays the current temperature and a 4-day forecast.
     */
    public void updateWeather() {
        weatherView.UpdateWeather((int) currentWeather.getTemperature(), currentWeather.getMainDescription());
        weatherView.updateForecastPane(filteredForecastWeather);
    }

    public String getCurrentWeatherDescription() {
        return currentWeather.getMainDescription();
    }

    public Weather getCurrentWeather() {
        return currentWeather;
    }

    private Weather getTodayWeather() {
        try {
            double[] coordinates = api.lookUpLocation("Tampere");
            double latitude = coordinates[0];
            double longitude = coordinates[1];
    
            // Fetch and process current weather data
            this.currentWeather = processWeather(api.currentWeatherAt(latitude, longitude));
    
            // Fetch and process forecast weather data
            this.forecastWeather = processForecastWeather(api.getForecastWeather(latitude, longitude, 0));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return currentWeather;
    }

}
