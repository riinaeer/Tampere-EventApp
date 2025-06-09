package fi.tuni.softwaredesign.eventapp.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A class to interact with the Visit Tampere API to fetch event data.
 */
public class EventApi {

    private final String baseUrl = "https://api.visittampere.com/api/v1/eventztoday/event/";
    private final List<String> outdoorEventNames = Arrays.asList("tori");
    private final List<String> outdoorWords = Arrays.asList("puisto", "ulko");
    private final List<String> outdoorLocations = Arrays.asList("puisto", "parkki", "ulkoilma","säävaraus");

    /**
     * Fetches events from the Visit Tampere API.
     * @return a list of Event objects containing event details.
     * @throws IOException if an I/O error occurs
     */
    public ArrayList<Event> getEvents() throws IOException {
        String apiUrl = constructApiUrl();
        System.out.println(apiUrl);
        String responseData = fetchDataFromAPI(apiUrl);
        String nextResponseUrl = parseNextResponseUrl(responseData);

        JsonArray eventsArray = parseEventResponse(responseData);

        ArrayList<Event> eventDetails = new ArrayList<>();

        while (nextResponseUrl != null) {
            String nextResponseData = fetchDataFromAPI(nextResponseUrl);
            JsonArray nextEventsArray = parseEventResponse(nextResponseData);
            eventsArray.addAll(nextEventsArray);
            nextResponseUrl = parseNextResponseUrl(nextResponseData);
        }

        for (JsonElement eventElement : eventsArray) {
            JsonObject event = eventElement.getAsJsonObject();
            String id = event.has("_id") ? event.get("_id").getAsString() : "";
            String name = event.has("name") ? event.get("name").getAsString() : "";
            String startTime = event.has("start_time") ? event.get("start_time").getAsString() : "";
            startTime = startTime.substring(0, 10);
            String endTime = event.has("end_time") ? event.get("end_time").getAsString() : "";
            endTime = endTime.substring(0, 10);
            String location = "";

            try {
                location = event.has("locations") ? event.get("locations").getAsJsonArray().get(0).getAsJsonObject().get("address").getAsString() : "";
            } catch (Exception e) {
                System.out.println("No location found for event: " + name);
            }

            String description = event.has("description") ? event.get("description").getAsString() : "";
            description = filterHtmlTagsFromText(description);
            // Categories
            ArrayList<String> categories = new ArrayList<>();
            JsonArray categoryArray = event.get("categories").getAsJsonArray();
            if (!categoryArray.isEmpty()) {
                for (JsonElement categoryElement : categoryArray) {
                    categories.add(categoryElement.getAsString());
                }
            }

            // Topics
            ArrayList<String> topics = new ArrayList<>();
            JsonArray topicArray = event.get("topics").getAsJsonArray();
            if (!topicArray.isEmpty()) {
                for (JsonElement topicElement : topicArray) {
                    topics.add(topicElement.getAsString());
                }
            }
            Boolean isIndoors = isEventIndoors(topics, description, location, name);

            // Add new Event-object to the list
            eventDetails.add(new Event(id, name, location, startTime, endTime, description, categories, topics, isIndoors));
        }
        return eventDetails;
    }

    /**
     * Checks if the event is indoors based on the event name, description, and location.
     * @param topics the topics of the event
     * @param description  the description of the event
     * @param location the location of the event
     * @param eventName the name of the event
     * @return true if the event is indoors, false otherwise
     */
    private Boolean isEventIndoors(ArrayList<String> topics, String description, String location, String eventName) {
        if (outdoorEventNames.stream().anyMatch(outdoorEventName -> eventName.toLowerCase().contains(outdoorEventName.toLowerCase()))) {
            return false;
        }
        if (outdoorWords.stream().anyMatch(outdoorWord -> description.toLowerCase().contains(outdoorWord.toLowerCase()))) {
            return false;
        }
        if (outdoorLocations.stream().anyMatch(outdoorLocation -> location.toLowerCase().contains(outdoorLocation.toLowerCase()))) {
            return false;
        }
        return true;
    }

    /**
     * Filters HTML tags from the text. 
     * @param text the text to filter
     * @return the filtered text
     */
    private String filterHtmlTagsFromText(String text) {
        String filteredText = text.replaceAll("</p>", "\n\n");
        filteredText = filteredText.replaceAll("<[^>]*>", "");
        filteredText = filteredText.replaceAll(":&nbsp;", " ");
        return filteredText;
    }

    /**
     * Constructs the API URL for fetching events.
     *
     * @param category the category of events
     * @param startDate the start date in ISO format
     * @param endDate the end date in ISO format
     * @return the constructed API URL as a string
     */
    private String constructApiUrl() {
        return baseUrl
                + "?format=json"
                + "&lang=fi"
                + "&limit=1000";
    }

    /**
     * Fetches data from the specified API URL.
     *
     * @param apiUrl the URL of the API endpoint
     * @return a string containing the response data
     * @throws IOException if an I/O error occurs
     */
    private String fetchDataFromAPI(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        StringBuilder response = new StringBuilder();
        try (Scanner scanner = new Scanner(connection.getInputStream(), "UTF-8")) {
            while (scanner.hasNextLine()) {
                response.append(scanner.nextLine());
            }
        }
        return response.toString();
    }

    /**
     * Parses the event response data from JSON format.
     *
     * @param responseData the JSON response data
     * @return a JsonArray containing the parsed event data
     */
    private JsonArray parseEventResponse(String responseData) {
        JsonObject jsonResponse = JsonParser.parseString(responseData).getAsJsonObject();
        return jsonResponse.getAsJsonArray("results");
    }

    /**
     * Parses the next response data from JSON format.
     *
     * @param responseData the JSON response data
     * @return a JsonArray containing the parsed next data
     */
    private String parseNextResponseUrl(String responseData) {
        JsonObject jsonResponse = JsonParser.parseString(responseData).getAsJsonObject();

        if (jsonResponse.has("next") && !jsonResponse.get("next").isJsonNull()) {
            return jsonResponse.get("next").getAsString();
        }
        return null;
    }
}
