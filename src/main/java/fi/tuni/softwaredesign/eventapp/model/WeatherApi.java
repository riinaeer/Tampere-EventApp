package fi.tuni.softwaredesign.eventapp.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A class to interact with the OpenWeatherMap API to fetch weather data.
 */
public class WeatherApi {

    private final String apiKey;

    public static final int CELSIUS = 0;
    public static final int FAHRENHEIT = 1;

    /**
     * Constructs a WeatherApi instance with the provided API key.
     *
     * @param apiKey the API key for accessing the OpenWeatherMap API
     */
    public WeatherApi(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Fetches the current weather for the given location.
     *
     * @param lat the latitude
     * @param lon the longitude
     * @return a Weather object representing the current weather
     * @throws IOException if an API call or data parsing fails
     */
    public Weather currentWeatherAt(double lat, double lon) throws IOException {
        String apiUrl = constructApiUrl("weather", lat, lon);
        String responseData = fetchDataFromAPI(apiUrl);
        JsonObject weatherData = parseWeatherResponse(responseData);

        double temperature = Double.parseDouble(extractTemperature(weatherData, CELSIUS));
        String mainDescription = normalizeWeatherType(
                weatherData.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString()
        );
        String description = weatherData.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

        return new Weather(temperature, 0, 0, 0, mainDescription, description, LocalDateTime.now());
    }

    /**
     * Looks up the geographical coordinates for the specified city name.
     *
     * @param cityName the name of the city
     * @return an array containing the latitude and longitude of the city
     * @throws IOException if an I/O error occurs or the location is not found
     */
    public double[] lookUpLocation(String cityName) throws IOException {
        // OpenWeatherMap Geocoding API URL
        String apiUrl = String.format("http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s", cityName, apiKey);
        String responseData = fetchDataFromAPI(apiUrl);

        // Parse the JSON response
        JsonArray locationDataArray = JsonParser.parseString(responseData).getAsJsonArray();

        if (!locationDataArray.isEmpty()) {
            JsonObject locationData = locationDataArray.get(0).getAsJsonObject();
            double latitude = locationData.get("lat").getAsDouble();
            double longitude = locationData.get("lon").getAsDouble();
            return new double[]{latitude, longitude};
        } else {
            throw new IOException("Location not found for city: " + cityName);
        }
    }

    /**
     * Fetches the weather forecast for the given location.
     *
     * @param lat the latitude
     * @param lon the longitude
     * @param tempUnit the temperature unit (CELSIUS or FAHRENHEIT)
     * @return an ArrayList of Weather objects representing the forecast
     * @throws IOException if an API call or data parsing fails
     */
    public ArrayList<Weather> getForecastWeather(double lat, double lon, int tempUnit) throws IOException {
        ArrayList<Weather> weatherList = new ArrayList<>();
        String apiUrl = String.format(Locale.US,
                "https://api.openweathermap.org/data/2.5/forecast?lat=%f&lon=%f&appid=%s&units=metric",
                lat, lon, apiKey
        );
        String responseData = fetchDataFromAPI(apiUrl);
        JsonObject jsonResponse = parseWeatherResponse(responseData);
        JsonArray list = jsonResponse.getAsJsonArray("list");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (JsonElement element : list) {
            JsonObject forecast = element.getAsJsonObject();
            double temperature = forecast.getAsJsonObject("main").get("temp").getAsDouble();

            double rain = forecast.has("rain")
                    ? forecast.getAsJsonObject("rain").get("3h").getAsDouble() : 0.0;

            String mainDescription = normalizeWeatherType(
                    forecast.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString()
            );
            String description = forecast.getAsJsonArray("weather").get(0).getAsJsonObject().get("description").getAsString();

            String dateTimeString = forecast.get("dt_txt").getAsString();
            LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, formatter);

            weatherList.add(new Weather(temperature, temperature, rain, 0, mainDescription, description, dateTime));
        }

        return weatherList;
    }

    /**
     * Fetches data from the specified API URL.
     *
     * @param apiUrl the URL of the API endpoint
     * @return the API response as a string
     * @throws IOException if the API call fails
     */
    private String fetchDataFromAPI(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        if (connection.getResponseCode() != 200) {
            throw new IOException("Failed to fetch data: HTTP " + connection.getResponseCode());
        }

        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream())) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }
        return response.toString();
    }

    /**
     * Parses a weather response JSON string.
     *
     * @param responseData the response data as a JSON string
     * @return the parsed JsonObject
     */
    private JsonObject parseWeatherResponse(String responseData) {
        return JsonParser.parseString(responseData).getAsJsonObject();
    }

    /**
     * Extracts the temperature from the weather data.
     *
     * @param weatherData the weather data as a JsonObject
     * @param tempUnit the temperature unit (CELSIUS or FAHRENHEIT)
     * @return the temperature as a string
     */
    private String extractTemperature(JsonObject weatherData, int tempUnit) {
        JsonObject main = weatherData.getAsJsonObject("main");
        double temperature = main.get("temp").getAsDouble();
        return tempUnit == CELSIUS
                ? String.format(Locale.US, "%.1f", temperature)
                : String.format(Locale.US, "%.1f", (temperature * 9 / 5) + 32);
    }

    /**
     * Normalizes weather types for consistent usage.
     *
     * @param mainDescription the main weather description
     * @return a normalized weather type string
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
     * Constructs the API URL for the given endpoint and location.
     *
     * @param endpoint the API endpoint (e.g., "weather" or "forecast")
     * @param lat the latitude
     * @param lon the longitude
     * @return the constructed API URL
     */
    private String constructApiUrl(String endpoint, double lat, double lon) {
        return String.format(Locale.US,
                "https://api.openweathermap.org/data/2.5/%s?lat=%f&lon=%f&appid=%s&units=metric",
                endpoint, lat, lon, apiKey
        );
    }
}
