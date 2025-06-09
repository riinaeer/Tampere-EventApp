package fi.tuni.softwaredesign.eventapp.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class for storing event information, uses EventApi to fetch data.
 */
public class EventModel {

    private static final String FILE_NAME = "events.dat";
    private List<Event> events;
    private EventApi api;
    private final int MIN_TEMPERATURE = -20;

    /**
     * Constructor for EventModel.
     */
    public EventModel() {
        this.events = new ArrayList<>();
        this.api = new EventApi();
    }

    /**
     * Fetches events from the API and populates the events list.
     * If successful, saves the updated events to a file.
     */
    public void fetchEventsFromApi() {
        try {
            List<Event> fetchedEvents = api.getEvents();
            events.clear(); // Clear existing events
            events.addAll(fetchedEvents); // Add fetched events to the list
            saveToFile(); // Save updated events to file
        } catch (IOException e) {
            System.err.println("Error fetching events: " + e.getMessage());
        }
    }

    /**
     * Saves the current list of events to a file.
     */
    public void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(events);
        } catch (IOException e) {
            System.err.println("Error saving events to file: " + e.getMessage());
        }
    }

    /**
     * Filters the events based on the weather. If the weather is poor, it filters out outdoor events.
     * @param events the list of events to filter
     * @param weather the weather to filter by
     * @return the filtered list of events
     */
    public List<Event> filterEventsByWeather(List<Event> events, Weather weather) {
        return events.stream()
       .filter(event -> {
           // If weather is bad only show indoor events
           if (weather.getMainDescription().toLowerCase().contains("rain") || 
               weather.getMainDescription().toLowerCase().contains("snow") || 
               weather.getMainDescription().toLowerCase().contains("thunder")) {
                if (!event.isIndoors()) {
                    System.out.println("Event: " + event.getName() + " is outdoors and will not be shown");
                }
               return event.isIndoors();
           } else if (weather.getTemperature() < MIN_TEMPERATURE) {
                return event.isIndoors();
           }
           // In good weather, show all events
           return true;
       })
       .collect(Collectors.toList());
    }

    /**
     * Loads the list of events from a file.
     */
    @SuppressWarnings("unchecked")
    public void loadFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No existing event file found. Starting fresh.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            events = (List<Event>) ois.readObject();
            System.out.println("Events successfully loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading events from file: " + e.getMessage());
            events = new ArrayList<>(); // Reset events to an empty list in case of error
        }
    }

    /**
     * Returns the list of events.
     *
     * @return the list of events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Filters the events for a specific date.
     *
     * @param events the list of events to filter
     * @param targetDate the date to filter by
     * @return the filtered list of events
     */
    public List<Event> filterEventsForDate(List<Event> events, LocalDate targetDate) {
        return events.stream()
                .filter(event -> {
                    // Parse start and end times as LocalDate
                    LocalDate eventStart = LocalDate.parse(event.getStartTime());
                    LocalDate eventEnd = LocalDate.parse(event.getEndTime());
                    LocalDate eventStartDate = eventStart;

                    // Only include events that start on our target date
                    return eventStartDate.equals(targetDate);
                })
                .collect(Collectors.toList());
    }

    /**
     * Filters the events for a specific category.
     * @param events the list of events to filter
     * @param category the category to filter by
     * @return the filtered list of events  
     */
    public List<Event> filterEventsForCategory(List<Event> events, List<String> category) {
        return events.stream()
                .filter(event -> event.getCategories().stream().anyMatch(category::contains))
                .collect(Collectors.toList());
    }

    /**
     * Filters the events for a specific title.
     * @param events the list of events to filter
     * @param title the title to filter by
     * @return the filtered list of events
     */
    public List<Event> filterEventsForTitle(List<Event> events, String title) {
        return events.stream()
                .filter(event -> event.getTitle().toLowerCase().contains(title.toLowerCase()))
                .collect(Collectors.toList());
    }
}
