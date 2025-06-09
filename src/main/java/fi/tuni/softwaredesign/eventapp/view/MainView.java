package fi.tuni.softwaredesign.eventapp.view;

import fi.tuni.softwaredesign.eventapp.controller.MainController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * MainView class is the main view of the application.
 */
public class MainView extends Application {

    private MainController mainController;
    private WeatherView weatherView;
    private EventsView eventsView;
    private Button toggleThemeButton;
    private Scene scene;


    /**
     * Constructor for MainView.
     */
    public MainView() {
        toggleThemeButton = new Button();
        toggleThemeButton.getStyleClass().add("toggle-theme-button");
        this.mainController = new MainController(this);
        this.weatherView = new WeatherView();
        this.eventsView = new EventsView();
    }

    /**
     * Start method for JavaFX application.
     */
    @Override
    public void start(Stage stage) {
        // Alusta WeatherController ja EventController
        mainController = new MainController(this);
        mainController.initializeControllers();

        // Define the scene width and height
        var sceneWidth = 1500.0;
        var sceneHeight = 900.0;

        // Create the UI layout
        var layout = new VBox();

        // Create a new box with a button aligned to the right
        var buttonBox = new HBox();
        buttonBox.getChildren().add(toggleThemeButton);
        HBox.setMargin(toggleThemeButton, new javafx.geometry.Insets(5, 5, 0, 0));
        buttonBox.setAlignment(Pos.TOP_RIGHT);

        // Add the button box to the layout
        layout.getChildren().add(buttonBox);

        // Add the weather and events views to the layout
        var weatherViewBox = weatherView.getWeatherViewBox();
        var eventViewBox = eventsView.getEventViewBox();

        // Make the boxes responsive to window size
        VBox.setVgrow(weatherViewBox, Priority.ALWAYS);
        VBox.setVgrow(eventViewBox, Priority.ALWAYS);

        layout.getChildren().addAll(weatherViewBox, eventViewBox);

        // Create the scene
        scene = new Scene(layout, sceneWidth, sceneHeight);

        var css = mainController.getCssString();
        scene.getStylesheets().add(css);

        // Show the stage
        stage.setScene(scene);
        stage.setTitle("Event App");
        stage.show();
    }

    /**
     * Launches the main view.
     * @param args the command line arguments
     */
    public void launchMainView(String[] args) {
        launch(args);
    }

    /**
     * Returns the main controller.
     * @return the main controller
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Returns the toggle theme button.
     * @return the toggle theme button
     */
    public Button getToggleThemeButton() {
        return toggleThemeButton;
    }

    /**
     * Returns the weather view.
     * @return the weather view
     */
    public WeatherView getWeatherView() {
        return weatherView;
    }

    /**
     * Returns the events view.
     * @return the events view
     */
    public EventsView getEventsView() {
        return eventsView;
    }
}
