package fi.tuni.softwaredesign.eventapp.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class for an Event
 */
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String location;
    private String startTime;
    private String endTime;
    private String description;
    private ArrayList<String> categories = new ArrayList<>();
    private ArrayList<String> topics = new ArrayList<>();
    private Boolean isIndoors;

    /**
     * Constructs a new Event with the given parameters.
     * @param id The id of the event.
     * @param name The name of the event.
     * @param location The location of the event.
     * @param startTime The start time of the event.
     * @param endTime The end time of the event.
     * @param description The description of the event.
     * @param categories The categories of the event.
     * @param topics The topics of the event.
     * @param isIndoors Whether the event is indoors.
     */
    public Event(String id, String name, String location, String startTime, String endTime, String description, ArrayList<String> categories, ArrayList<String> topics, Boolean isIndoors) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.categories = (categories != null) ? categories : new ArrayList<>();
        this.topics = (topics != null) ? topics : new ArrayList<>();
        this.isIndoors = isIndoors;
    }

    /**
     * Returns the id of the event.
     * @return The id of the event.
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the event.
     * @return The name of the event.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the location of the event.
     * @return The location of the event.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the start time of the event.
     * @return The start time of the event.
     */
    public String getStartTime() {
        return startTime;
    }

    /**
     * Returns the end time of the event.
     * @return The end time of the event.
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * Returns the description of the event.
     * @return The description of the event.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the title of the event.
     * @return The title of the event.
     */
    public String getTitle() {
        return name;
    }


    /**
     * Returns the date of the event.
     * @return The date of the event.
     */
    public String getDate() {
        return startTime;
    }

    /**
     * Returns whether the event is indoors.
     * @return True if the event is indoors, false otherwise.
     */
    public Boolean isIndoors() {
        return isIndoors;
    }

    /**
     * Returns the categories of the event.
     * @return The categories of the event.
     */
    public ArrayList<String> getCategories() {
        return categories != null ? categories : new ArrayList<>();
    }

    /**
     * Returns the topics of the event.
     * @return The topics of the event.
     */
    public ArrayList<String> getTopics() {
        return topics != null ? topics : new ArrayList<>();
    }

    /**
     * Returns the first topic of the event.
     * @return The first topic of the event.
     */
    public String getTopic() {
        if (topics != null && !topics.isEmpty()) {
            return topics.get(0);
        }
        return "";
    }

    /**
     * Returns the first category of the event.
     * @return The first category of the event.
     */
    public String getCategory() {
        if (categories != null && !categories.isEmpty()) {
            return categories.get(0);
        }
        return "";
    }

    /**
     * Returns a string representation of the event.
     * @return A string representation of the event.
     */
    @Override
    public String toString() {
        return "Event: " + name + ", Location: " + location + ", Start: " + startTime + ", End: " + endTime;
    }
}
