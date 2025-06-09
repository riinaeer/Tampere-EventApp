package fi.tuni.softwaredesign.eventapp.view;

import java.time.LocalDate;
import java.util.ArrayList;
import fi.tuni.softwaredesign.eventapp.model.Weather;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * WeatherView class for the weather information
 */
public class WeatherView {

    private HBox weatherViewBox;
    private HBox upperLeftBox;
    private HBox temperatureBox;
    private HBox bottomBox;
    private Button leftButton;
    private Button rightButton;
    private Label temperatureLabel;
    private DatePicker datePicker;
    private FlowPane forecastPane;
    private ImageView weatherIcon; // Added to store the dynamic weather icon

    /**
     * Constructor for WeatherView.
     */
    public WeatherView() {
        createUpperLeftBox();
        createTemperatureBox();
        createBottomBox();
        this.forecastPane = createForecastPane();
        createWeatherBox();
    }

    /**
     * Creates the main weather view box.
     */
    private void createWeatherBox() {
        this.weatherViewBox = new HBox(30); // Main HBox
        weatherViewBox.getStyleClass().add("weather-view-box");

        var leftBox = new VBox(); // Left container for current weather
        leftBox.setAlignment(Pos.CENTER);
        leftBox.getStyleClass().add("left-box");

        var rightBox = new VBox(10); // Right container for forecast
        rightBox.getStyleClass().add("right-box");
        rightBox.setAlignment(Pos.TOP_CENTER);

        var forecastTitle = new Label("Weather Forecast");
        forecastTitle.getStyleClass().add("forecast-title");
        rightBox.getChildren().addAll(forecastTitle, forecastPane);

        leftBox.getChildren().addAll(getUpperLeftBox(), getTemperatureBox(), getBottomBox());

        HBox.setHgrow(leftBox, Priority.ALWAYS);
        HBox.setHgrow(rightBox, Priority.ALWAYS);

        weatherViewBox.getChildren().addAll(leftBox, rightBox);

        // Adjust widths based on total width
        weatherViewBox.widthProperty().addListener((observable, oldValue, newValue) -> {
            double totalWidth = newValue.doubleValue();
            leftBox.setMaxWidth(totalWidth * 0.40);
            rightBox.setMaxWidth(totalWidth * 0.60);
        });
    }

    /**
     * Creates the forecast pane.
     *
     * @return the FlowPane for forecasts
     */
    private FlowPane createForecastPane() {
        FlowPane forecastPane = new FlowPane();
        forecastPane.getStyleClass().add("weather-forecast");

        forecastPane.setHgap(30);
        forecastPane.setVgap(16);
        forecastPane.setAlignment(Pos.CENTER);

        return forecastPane;
    }

    /**
     * Updates the forecast pane with forecasts.
     *
     * @param forecasts List of Weather objects
     */
    public void updateForecastPane(ArrayList<Weather> forecasts) {
        forecastPane.getChildren().clear();
        for (Weather forecast : forecasts) {
            VBox dayBox = createForecastCard(forecast);
            forecastPane.getChildren().add(dayBox);
        }
    }

    /**
     * Updates the forecast pane with a message when no forecast data is
     * available.
     *
     * @param message The message to display
     */
    public void updateForecastPaneWithMessage(String message) {
        forecastPane.getChildren().clear();
        Label noDataLabel = new Label(message);
        noDataLabel.getStyleClass().add("no-data-label");
        forecastPane.setAlignment(Pos.CENTER);
        forecastPane.getChildren().add(noDataLabel);
    }

    /**
     * Creates a forecast card.
     *
     * @param forecast Weather object for a day
     * @return VBox representing the card
     */
    private VBox createForecastCard(Weather forecast) {
        VBox dayBox = new VBox(10);
        dayBox.setAlignment(Pos.CENTER);
        dayBox.getStyleClass().add("day-box");

        Label dayLabel = new Label(forecast.getDateTime().getDayOfWeek().toString());
        dayLabel.getStyleClass().add("day-label");

        String weatherType = forecast.getMainDescription();
        Image icon = new Image(getClass().getResourceAsStream("/icons/weather/" + weatherType + ".png"));
        ImageView weatherIcon = new ImageView(icon);
        weatherIcon.setFitHeight(65);
        weatherIcon.setPreserveRatio(true);
        weatherIcon.getStyleClass().add("forecast-weather-icon");

        Label tempLabel = new Label((int) Math.round(forecast.getTemperature()) + "°C");
        tempLabel.getStyleClass().add("forecast-temp-label");

        HBox iconTempBox = new HBox(20);
        iconTempBox.setAlignment(Pos.CENTER);
        iconTempBox.getChildren().addAll(weatherIcon, tempLabel);

        dayBox.getChildren().addAll(dayLabel, iconTempBox);
        return dayBox;
    }

    /**
     * Creates the upper left box with date picker and navigation buttons.
     */
    private void createUpperLeftBox() {
        this.upperLeftBox = new HBox(10);
        this.upperLeftBox.setAlignment(Pos.CENTER);

        this.leftButton = new Button();
        leftButton.getStyleClass().add("left-arrow-button");

        this.datePicker = new DatePicker(LocalDate.now());
        datePicker.setEditable(false);
        datePicker.getStyleClass().add("date-picker");

        this.rightButton = new Button();
        rightButton.getStyleClass().add("right-arrow-button");
        upperLeftBox.getChildren().addAll(leftButton, datePicker, rightButton);
    }

    /**
     * Creates the temperature box with weather icon and temperature label.
     */
    private void createTemperatureBox() {
        this.temperatureBox = new HBox(20);
        this.temperatureBox.setAlignment(Pos.CENTER);

        this.weatherIcon = new ImageView();
        weatherIcon.setFitHeight(100);
        weatherIcon.setPreserveRatio(true);
        weatherIcon.getStyleClass().add("weather-icon");

        this.temperatureLabel = new Label("0°C");
        temperatureLabel.getStyleClass().add("temperature-label");

        temperatureBox.getChildren().addAll(weatherIcon, temperatureLabel);
        temperatureBox.getStyleClass().add("temperature-box");
    }

    /**
     * Creates the bottom box with location information.
     */
    private void createBottomBox() {
        Label locationIcon = new Label();
        locationIcon.getStyleClass().add("location-icon");

        var location = "Tampere, Finland";
        var locationLabel = new Label(location);
        locationLabel.getStyleClass().add("location-label");

        var locationBox = new HBox(10);
        locationBox.setAlignment(Pos.CENTER);
        locationBox.getChildren().addAll(locationIcon, locationLabel);
        locationBox.getStyleClass().add("location-box");

        var emptyBox = new VBox();
        this.bottomBox = new HBox(30);
        bottomBox.getChildren().addAll(emptyBox, locationBox);

        HBox.setHgrow(emptyBox, Priority.ALWAYS);
        HBox.setHgrow(locationBox, Priority.ALWAYS);
    }

    /**
     * Updates the current weather display.
     *
     * @param temperature Current temperature, or 0 if no data is available
     * @param weatherType Current weather type or an error message if no data is
     * available
     */
    public void UpdateWeather(int temperature, String weatherType) {
        if (weatherType.startsWith("No weather data available")) {
            // Display the error message and remove weather icon
            temperatureLabel.setText(weatherType);
            temperatureLabel.setStyle("-fx-font-size: 16px; -fx-text-alignment: center; -fx-wrap-text: true;");
            weatherIcon.setImage(null);
        } else {
            // Restore original styles for normal weather data
            temperatureLabel.setText(temperature + "°C");
            temperatureLabel.setStyle("-fx-font-size: 36px; -fx-font-weight: bold;");
            String iconPath = String.format("/icons/weather/%s.png", weatherType);
            Image icon = new Image(getClass().getResourceAsStream(iconPath));
            weatherIcon.setImage(icon);
        }
    }

    /**
     * Returns the left button.
     * @return the left button
     */
    public Button getLeftButton() {
        return leftButton;
    }

    /**
     * Returns the right button.
     * @return the right button
     */
    public Button getRightButton() {
        return rightButton;
    }

    /**
     * Returns the date picker.
     * @return the date picker
     */
    public HBox getWeatherViewBox() {
        return weatherViewBox;
    }

    /**
     * Returns the upper left box.
     * @return the upper left box
     */
    public HBox getUpperLeftBox() {
        return upperLeftBox;
    }

    /**
     * Returns the temperature box.
     * @return the temperature box
     */
    public HBox getTemperatureBox() {
        return temperatureBox;
    }

    /**
     * Returns the bottom box.
     * @return the bottom box
     */
    public HBox getBottomBox() {
        return bottomBox;
    }

    /**
     * Returns the date picker.
     * @return the date picker
     */
    public DatePicker getDatePicker() {
        return datePicker;
    }

}
