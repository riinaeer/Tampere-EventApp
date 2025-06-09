package fi.tuni.softwaredesign.eventapp.controller;

import java.time.LocalDate;

import fi.tuni.softwaredesign.eventapp.view.MainView;
import javafx.scene.Scene;

/**
 * MainController handles the main view, and combines the other controllers.
 */
public class MainController {
  
  private MainView mainView;
  private boolean isDarkMode = false;
  private WeatherController weatherController;
  private EventController eventController;

  /**
   * Constructs a new MainController with the given MainView.
   * @param mainView The MainView to use.
   */
  public MainController(MainView mainView) {
    this.mainView = mainView;

    mainView.getToggleThemeButton().setOnAction(event -> toggleTheme());
  }

  /**
   * Returns the CSS string for the current theme.  
   * @return The CSS string for the current theme.
   */
  public String getCssString() {
    var css = isDarkMode ? "/css/dark-style.css" : "/css/light-style.css";
    return css;
  }

  /**
   * Toggles the theme between light and dark mode.
   */
  public void toggleTheme() {
    // Toggle the theme
    isDarkMode = !isDarkMode;

    // Update the CSS on the scene
    Scene scene = mainView.getScene();
    scene.getStylesheets().clear();
    scene.getStylesheets().add(getCssString());
  }

  /**
   * Returns whether the current theme is dark mode.
   * @return True if the current theme is dark mode, false otherwise.
   */
  public boolean isDarkMode() {
    return isDarkMode;
  }

  /**
   * Initializes the controllers for the main view.
   */
  public void initializeControllers() {
       weatherController = new WeatherController(mainView.getWeatherView());
       eventController = new EventController(mainView.getEventsView(), weatherController.getCurrentWeather());
       setupDatePickerListener();
   }

   /**
    * Sets up a listener for the date picker in the weather view.
    */
  private void setupDatePickerListener() {
      mainView.getWeatherView().getDatePicker().setOnAction(event -> {
          LocalDate selectedDate = mainView.getWeatherView().getDatePicker().getValue();
          if (selectedDate != null) {
              weatherController.updateWeatherForSelectedDate(selectedDate, eventController);
              eventController.updateEventsForSelectedDate(selectedDate, weatherController.getCurrentWeather());
          }
      });
  }
}
