package fi.tuni.softwaredesign.eventapp;

import fi.tuni.softwaredesign.eventapp.view.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * An application for displaying weather, and event recommendations based on weather
 * 
 * Software Design course, Tampere University
 * 
 * Authors (in aplhabetical order):
 * 
 * Jussa Jutila (jussa.jutila@tuni.fi)
 * Pyry Mäkinen (pyry.makinen@tuni.fi)
 * Riina Peltonen (riina.peltonen@tuni.fi)
 * Tiina Tamminen (tiina.tamminen@tuni.fi)
 * 
 */
public class EventApp extends Application {
  private MainView mainView;

  /**
   * Start method for JavaFX application.
   */
  @Override
  public void start(Stage primaryStage) {
    mainView = new MainView();

    mainView.start(primaryStage);
  }

  /**
   * Main method for the application.
   * @param args Command line arguments.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
