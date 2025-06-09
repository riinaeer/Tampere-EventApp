package fi.tuni.softwaredesign.eventapp.controller;

import java.time.LocalDate;
import java.util.List;

import fi.tuni.softwaredesign.eventapp.model.Event;
import fi.tuni.softwaredesign.eventapp.model.EventModel;
import fi.tuni.softwaredesign.eventapp.model.Weather;
import fi.tuni.softwaredesign.eventapp.view.EventsView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

/**
 * Handles communication between Model and View, manages fetching data from
 * API's, processes the data, updates view etc.
 */
public class EventController {

    private final EventModel eventModel;
    private final EventsView eventsView;
    private LocalDate currentDateOnly;
    private List<Event> events;
    private List<Event> filteredEvents;
    private List<Event> currentEvents;
    private List<Event> searchEvents;

    /**
     * Constructs a new EventController with the given EventsView and Weather.
     * @param eventsView The EventsView to use.
     * @param weather The Weather to use.
     */
    public EventController(EventsView eventsView, Weather weather) {
        this.eventModel = new EventModel();
        this.eventsView = eventsView;

        eventsView.getSearchButton().setOnAction(event -> handleSearch());
        eventsView.getCategoryBox().setOnAction(event -> handleCategory());
        eventsView.getSearchField().setOnAction(event -> handleSearch());

        initialize(weather);
    }

    /**
     * Initializes the controller after construction.
     * @param weather The Weather to use.
     * @param events The list of events to use.
     */
    private void initialize(Weather weather) {
        this.currentDateOnly = LocalDate.now();

        eventModel.loadFromFile();

        if (eventModel.getEvents().isEmpty()) {
            eventModel.fetchEventsFromApi();
        }
        this.events = eventModel.getEvents();
        this.currentEvents = eventModel.filterEventsForDate(events, currentDateOnly);
        this.filteredEvents = eventModel.filterEventsByWeather(currentEvents, weather);
        eventsView.updateEvents(filteredEvents);
    }

    /**
     * Updates the event flow pane with events from the model.
     * @param events The list of events to use.
     */
    public void updateEventFlowPane(List<Event> events) {
        FlowPane flowPane = eventsView.getEventFlowPane();
        flowPane.getChildren().clear();

        for (Event event : events) {
            VBox eventCard = eventsView.createEventCard(event);
            flowPane.getChildren().add(eventCard);
        }
    }

    /**
     * Handles the search button click event.
     */
    private void handleSearch() {
        String searchString = eventsView.getSearchField().getText();
        this.searchEvents = eventModel.filterEventsForTitle(filteredEvents, searchString);
        updateEventFlowPane(searchEvents);
    }

    /**
     * Handles the calendar and gives events for selected date, filtered by weather
     * @param selectedDate The selected date
     * @param weather Weather
     */
    public void updateEventsForSelectedDate(LocalDate selectedDate, Weather weather) {
        currentDateOnly = selectedDate;
        currentEvents = eventModel.filterEventsForDate(events, currentDateOnly);
        filteredEvents = eventModel.filterEventsByWeather(currentEvents, weather);
        eventsView.updateEvents(filteredEvents);
    }

    /**
     * Handles the category selection event.
     */
    private void handleCategory() {
        String category = eventsView.getCategoryBox().getValue();
        switch (category) {
            case "All":
                this.filteredEvents = currentEvents;
                break;
            case "Bars and Nightlife":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Baarit ja yöelämä"));
                break;
            case "Performing Arts":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Esittävät taiteet", "Muu esittävä taide"));
                break;
            case "History":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Historia", "Historiallinen nähtävyys"));
                break;
            case "Christmas":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Joulu"));
                break;
            case "Ice Hockey":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Jääkiekko"));
                break;
            case "Cafes":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Kahvilat"));
                break;
            case "Meeting Place":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Kokouspaikka"));
                break;
            case "Cultural Heritage":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Kulttuuriperintö"));
                break;
            case "Handicrafts":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Käsityöt"));
                break;
            case "Walking and Hiking":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Kävely ja vaeltaminen", "Patikointi"));
                break;
            case "Children's Attraction":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Lapsille"));
                break;
            case "Museums and Galleries":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Museot ja galleriat"));
                break;
            case "Music":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Musiikki"));
                break;
            case "Other Indoor Activity":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Muu sisäaktiviteetti"));
                break;
            case "Others":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Muut"));
                break;
            case "Restaurant":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Ravintola"));
                break;
            case "Food Experiences":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Ruokaelämykset", "Ruokaelämys"));
                break;
            case "Stand Up":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Stand up"));
                break;
            case "Finnish Design and Fashion":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Suomalainen design ja muoti"));
                break;
            case "Art":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Taide"));
                break;
            case "Dance":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Tanssi"));
                break;
            case "Event":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Tapahtuma"));
                break;
            case "Event Venue":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Tapahtumapaikka"));
                break;
            case "Events and Festivals":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Tapahtumat ja festivaalit"));
                break;
            case "Theater":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Teatteri"));
                break;
            case "Markets":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Torit"));
                break;
            case "Sports":
                this.filteredEvents = eventModel.filterEventsForCategory(currentEvents, List.of("Urheilu"));
                break;
            default:
                this.filteredEvents = currentEvents;
        }
        updateEventFlowPane(filteredEvents);
    }

    /**
     * Returns the eventModel
     * @return the EventModel instance
     */
    public EventModel getEventModel() {
        return eventModel;
    }

    /**
     * Returns the eventsView
     * @return the EventsView instance
     */
    public EventsView getEventsView() {
        return eventsView;
    }
}
