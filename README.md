# EventApp

## Project Overview

**EventApp** is a JavaFX-based application that provides an interface for displaying events and weather information. The application fetches event data from the **Visit Tampere API** and weather data from the **OpenWeatherMap API**. It displays all events on the main page, categorized and filtered based on user preferences and weather conditions. Additionally, it provides a 5-day weather information to help users plan their activities.

---

## Environment Setup

### Prerequisites

To run the application, ensure the following tools and dependencies are installed on your system:

- **Java Development Kit (JDK) 17** or higher
  - Download from [AdoptOpenJDK](https://adoptopenjdk.net/).
- **Maven** (for dependency management and building the project)
  - Install it from [Maven Download](https://maven.apache.org/download.cgi).

### Required Libraries

Dependencies are managed by Maven and will be automatically downloaded during the build process. Key dependencies include:

- **JavaFX** (version 17 or higher) for the graphical user interface.
- **Gson** for parsing JSON data.
- Any additional libraries required for handling HTTP requests and API interactions.

---

## Features

1. **Events Display**:
   - Fetches event data from the **Visit Tampere API**.
   - Displays events categorized by type (e.g., Art, Music, Sports).
   - Filters events based on weather conditions (e.g., only indoor events for rainy or snowy days).
   - Provides a search functionality for event titles.
   - Displays events in both Finnish and English (depending on data availability).

2. **Weather Information**:
   - Fetches current weather data for Tampere from the **OpenWeatherMap API**.
   - Provides a 5-day weather forecast to aid event planning.
   - Displays weather details, including temperature, condition, and an appropriate icon.

3. **Dynamic Themes**:
   - Allows users to toggle between light and dark themes for the interface.

4. **Localized Data**:
   - Filters events intelligently based on location keywords, event names, and descriptions.

---

## Project Structure

The source code is organized as follows:

### **Main Directory**: `src/main/java/fi/tuni/softwaredesign/eventapp/`

#### Core Files:
- **EventApp.java**: The main entry point of the application.
- **SystemInfo.java**: Retrieves system information such as Java and JavaFX versions.

#### Controllers:
- **MainController.java**:
  - Combines weather and event controllers into a unified interface.
  - Handles theme toggling and initialization of views and controllers.
- **EventController.java**:
  - Manages event data fetching, filtering, and displaying.
  - Handles interactions between the event model and the event view.
- **WeatherController.java**:
  - Manages weather data fetching and updates the weather view.
  - Filters weather forecasts and handles date selection logic.

#### Models:
- **EventModel.java**:
  - Handles event data storage and provides filtering methods for categories, weather, and dates.
  - Uses **EventApi.java** for fetching event data.
- **EventApi.java**:
  - Interacts with the **Visit Tampere API** to fetch event data.
  - Differentiates between indoor and outdoor events based on keywords in event names, descriptions, and locations.
- **Event.java**:
  - Represents an individual event.
- **Weather.java**:
  - Represents weather data with properties such as temperature, condition, wind speed, and rain amount.
- **WeatherApi.java**:
  - Interacts with the **OpenWeatherMap API** to fetch current weather and forecasts.

#### Views:
- **MainView.java**:
  - Combines the weather and event views into a single interface.
  - Provides buttons for theme toggling and navigation.
- **EventsView.java**:
  - Displays event cards with categories, icons, descriptions, and a "Read More" feature.
  - Supports search and category selection.
- **WeatherView.java**:
  - Displays current weather details and a 4-day forecast in a visually appealing layout.
  - Includes navigation buttons and a date picker.

---

## How to Run the Application

1. Clone the repository to your local machine.
2. Ensure JDK 17 and Maven are installed and properly configured.
3. Open the project in your preferred IDE (e.g., IntelliJ IDEA, Eclipse).
4. Build the project using Maven:
   ```bash
   mvn clean install
5. Run application:
  ```bash
   mvn javafx:run
