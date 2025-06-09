package fi.tuni.softwaredesign.eventapp.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import fi.tuni.softwaredesign.eventapp.model.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/**
 * EventsView class for displaying event cards.
 */
public class EventsView {

    private static final int HORIZONTAL_GAP = 40;
    private static final int VERTICAL_GAP = 20;
    private static final int PADDING = 10;
    private static final int EVENT_CARD_WIDTH = 400;
    private static final int EVENT_CARD_SPACING = 10;
    private static final int SEARCH_SPACING = 10;

    private VBox eventViewBox;
    private TextField searchField;
    private Button searchButton;
    private FlowPane eventFlowPane;
    private ComboBox<String> categoryBox;

    // Mapping of keywords to icon names
    private static final Map<String, String> keywordIconMap = new HashMap<>();

    static {
        keywordIconMap.put("joulu", "christmas.png");
        keywordIconMap.put("jääkiekko", "ice-hockey.png");
        keywordIconMap.put("hockey", "ice-hockey.png");
        keywordIconMap.put("tappara", "ice-hockey.png");
        keywordIconMap.put("ilves", "ice-hockey.png");
        keywordIconMap.put("näyttely", "art.png");
        keywordIconMap.put("stand up", "stand-up.png");
        keywordIconMap.put("teatteri", "theater.png");
        keywordIconMap.put("tanssi", "dance.png");
        keywordIconMap.put("disco", "dance.png");
        keywordIconMap.put("musical", "music.png");
        keywordIconMap.put("konsertti", "music.png");
        keywordIconMap.put("band", "music.png");
        keywordIconMap.put("laula", "music.png");
        keywordIconMap.put("pubi", "bars-nightlife.png");
    }

    /**
     * Constructor to initialize the EventsView.
     */
    public EventsView() {
        createEventsBox();
    }

    /**
     * Creates the main container for the events view.
     */
    private void createEventsBox() {
        eventViewBox = new VBox(10);
        eventViewBox.getStyleClass().add("event-view-box");

        HBox topBar = createTopBar();
        ScrollPane scrollPane = createScrollPane();

        // Add the top bar and scroll pane to the eventsBox
        eventViewBox.getChildren().addAll(topBar, scrollPane);

        // Allow the eventsBox to grow if needed
        VBox.setVgrow(eventViewBox, Priority.ALWAYS);
    }

    /**
     * Creates the top bar with a label and search functionality.
     *
     * @return the top bar HBox
     */
    private HBox createTopBar() {
        HBox topBar = new HBox();
        Label label = new Label("Recommended Events");

        categoryBox = createCategoryBox();

        searchField = new TextField();
        searchField.setPromptText("Search for an event...");
        searchField.getStyleClass().add("search-field");
        searchField.setPrefWidth(250);

        searchButton = new Button("");
        searchButton.getStyleClass().add("search-button");

        // Align search field and button to the right
        HBox searchBox = new HBox(SEARCH_SPACING, categoryBox, searchField, searchButton);
        searchBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(searchBox, Priority.ALWAYS);

        // Add components to the top bar
        topBar.getChildren().addAll(label, searchBox);

        return topBar;
    }

    /**
     * Creates a combo box for selecting event categories.
     *
     * @return the category combo box
     */
    private ComboBox<String> createCategoryBox() {
        categoryBox = new ComboBox<>();
        categoryBox.setPromptText("Select a category");
        categoryBox.getStyleClass().add("category-box");
        categoryBox.setPrefWidth(200);
        categoryBox.getItems().addAll(
          "All",
          "Art",
          "Bars and Nightlife",
          "Cafes",
          "Children's Attraction",
          "Christmas",
          "Cultural Heritage",
          "Dance",
          "Event",
          "Event Venue",
          "Events and Festivals",
          "Finnish Design and Fashion",
          "Food Experiences",
          "Handicrafts",
          "History",
          "Ice Hockey",
          "Markets",
          "Meeting Place",
          "Museums and Galleries",
          "Music",
          "Other Indoor Activity",
          "Others",
          "Performing Arts",
          "Restaurant",
          "Sports",
          "Stand Up",
          "Theater",
          "Walking and Hiking"
        );
        return categoryBox;
    }

    /**
     * Creates a scroll pane containing a flow pane for event cards.
     *
     * @return the scroll pane
     */
    private ScrollPane createScrollPane() {
        // Create a FlowPane for event cards
        eventFlowPane = new FlowPane();
        eventFlowPane.setHgap(HORIZONTAL_GAP);
        eventFlowPane.setVgap(VERTICAL_GAP);
        eventFlowPane.setPadding(new Insets(PADDING));
        eventFlowPane.setAlignment(Pos.CENTER);

        // Create a ScrollPane for the event flow pane
        ScrollPane scrollPane = new ScrollPane(eventFlowPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.getStyleClass().add("scroll-pane");

        // Center the flow pane content within the ScrollPane
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        return scrollPane;
    }

    /**
     * Creates an event card with the given event data.
     *
     * @param event the event data
     * @return the event card VBox
     */
    public VBox createEventCard(Event event) {
        VBox eventCard = new VBox(EVENT_CARD_SPACING);
        eventCard.getStyleClass().add("event-card");
        eventCard.setPrefWidth(EVENT_CARD_WIDTH);

        // Create the icon, title, date, and location at the top
        ImageView eventIcon = fetchEventIcon(event);
        Label titleLabel = createLabel(event.getTitle(), "event-title");

        String formattedDate = event.getDate().substring(8, 10) + "." + event.getDate().substring(5, 7) + "." + event.getDate().substring(0, 4);
        Label dateLabel = createLabel(formattedDate, "event-date");

        Label locationLabel = createLabel(event.getLocation(), "event-location");

        HBox dateAndLocationBox = new HBox(EVENT_CARD_SPACING);
        dateAndLocationBox.getChildren().addAll(dateLabel, locationLabel);
        dateAndLocationBox.getStyleClass().add("date-location-box");

        // Short description
        Label shortDescriptionLabel = createLabel(shortenDescription(event.getDescription()), "event-description");
        shortDescriptionLabel.setWrapText(true);

        // Hide the original long description
        Label fullDescriptionLabel = createLabel(event.getDescription(), "event-full-description");
        fullDescriptionLabel.setVisible(false);
        fullDescriptionLabel.setManaged(false);

        Button readMoreButton = new Button("Read More");
        readMoreButton.getStyleClass().add("read-more-button");
        readMoreButton.setOnAction(e -> {
          boolean isExpanded = fullDescriptionLabel.isVisible();
          fullDescriptionLabel.setVisible(!isExpanded);
          fullDescriptionLabel.setManaged(!isExpanded);
          shortDescriptionLabel.setVisible(isExpanded);
          shortDescriptionLabel.setManaged(isExpanded);
          readMoreButton.setText(isExpanded ? "Read More" : "Show Less");
        });
        
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        //Group content above the "Read More" button
        VBox contentBox = new VBox(EVENT_CARD_SPACING);
        contentBox.getChildren().addAll(eventIcon, titleLabel, dateAndLocationBox, shortDescriptionLabel, fullDescriptionLabel);
        contentBox.setAlignment(Pos.CENTER);
        
        // Add all elements to the event card
        eventCard.getChildren().addAll(contentBox, spacer);

        // Add the "Read More" button if the description is too long
        if (event.getDescription().length() > 300) {
            eventCard.getChildren().addAll(readMoreButton);
        }
        eventCard.setAlignment(Pos.CENTER);

        return eventCard;
    }

    /**
     * Shortens the description into approx 10 lines
     *
     * @param description original description
     * @return shortened description
     */
    private String shortenDescription(String description) {
        int maxLength = 600;
        if (description.length() > maxLength) {
            return description.substring(0, maxLength) + "...";
        }
        return description;
    }

    /**
     * Fetches the event icon based on the event category and keywords.
     *
     * @param event the event data
     * @return the event icon ImageView, defaulting to a generic event icon if not found
     */
    private ImageView fetchEventIcon(Event event) {
      String iconName = getCategoryIconName(event.getCategory());
      
      // Check for keywords in the title to determine the icon
      String keywordIconName = getKeywordIconName(event.getTitle());
      if (keywordIconName != null) {
          iconName = keywordIconName; // Override with keyword icon if found
      }

      Image icon;
      try {
          icon = new Image(getClass().getResourceAsStream("/icons/categories/" + iconName));
          if (icon.isError()) {
              throw new NullPointerException();
          }
      } catch (NullPointerException e) {
          // Use a generic event icon if the category icon is not found
          icon = new Image(getClass().getResourceAsStream("/icons/categories/event.png"));
      }
      ImageView eventIcon = new ImageView(icon);
      return eventIcon;
    }

    /**
     * Determines the icon name based on keywords in the title or description.
     *
     * @param title the event title
     * @return the icon name if a keyword matches, otherwise null
     */
    private String getKeywordIconName(String title) {
        for (Map.Entry<String, String> entry : keywordIconMap.entrySet()) {
            String keyword = entry.getKey();
            String icon = entry.getValue();
            
            if (title.toLowerCase().contains(keyword.toLowerCase())) {
                return icon; // Return the corresponding icon if a keyword is found
            }
        }
        return null; // No matching keyword found
    }

    /**
     * Chooses the right icon name for the event.
     * 
     * @param string the category of the event
     * @return the icon name
     */
    private String getCategoryIconName(String category) {
        if (category == null || category.isEmpty()) {
            return "event.png";
        } else {
            switch (category) {
          case "Baarit ja yöelämä":
              return "bars-nightlife.png";
          case "Esittävät taiteet":
              return "performance-arts.png";
          case "Historia":
          case "Historiallinen nähtävyys":
              return "cultural-heritage.png";
          case "Joulu":
              return "christmas.png";
          case "Jääkiekko":
              return "ice-hockey.png";
          case "Kahvilat":
              return "cafes.png";
          case "Kokouspaikka":
              return "meeting-place.png";
          case "Kulttuuriperintö":
              return "cultural-heritage.png";
          case "Käsityöt":
              return "handicraft.png";
          case "Kävely ja vaeltaminen":
          case "Patikointi":
              return "hiking.png";
          case "Lapsille":
              return "children.png";
          case "Museot ja galleriat":
              return "museums-galleries.png";
          case "Musiikki":
              return "music.png";
          case "Muu sisäaktiviteetti":
          case "Muut":
              return "other-activity.png";
          case "Ravintola":
              return "restaurant.png";
          case "Ruokaelämykset":
          case "Ruokaelämys":
              return "food-experience.png";
          case "Stand up":
              return "stand-up.png";
          case "Suomalainen design ja muoti":
              return "finnish-design-fashion.png";
          case "Taide":
              return "art.png";
          case "Tanssi":
              return "dance.png";
          case "Tapahtuma":
          case "Tapahtumapaikka":
              return "event.png";
          case "Tapahtumat ja festivaalit":
              return "events-festivals.png";
          case "Teatteri":
              return "theater.png";
          case "Torit":
              return "market.png";
          case "Urheilu":
              return "sports.png";
          default:
              return "event.png";
          }
        }
    }

    /**
     * Creates a label with the given text and style class.
     *
     * @param text the label text
     * @param styleClass the style class
     * @return the label
     */
    private Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.getStyleClass().add(styleClass);
        return label;
    }

    /**
     * Returns the main container for the events view.
     * @return the event view VBox
     */
    public VBox getEventViewBox() {
        return eventViewBox;
    }

    /**
     * Returns the search field for filtering events.
     * @return the search field TextField
     */
    public TextField getSearchField() {
        return searchField;
    }

    /**
     * Returns the search button for filtering events.
     * @return the search button Button
     */
    public Button getSearchButton() {
        return searchButton;
    }

    /**
     * Returns the flow pane for displaying event cards.
     * @return the event flow pane FlowPane
     */
    public FlowPane getEventFlowPane() {
        return eventFlowPane;
    }

    /**
     * Returns the category box for filtering events.
     * @return the category box ComboBox
     */
    public ComboBox<String> getCategoryBox() {
        return categoryBox;
    }

    /**
     * Updates the events displayed in the view.
     * @param events the list of events to display
     */
    public void updateEvents(List<Event> events) {
        // Handle all UI updates here
        eventFlowPane.getChildren().clear();
        events.forEach(event -> {
            VBox eventCard = createEventCard(event);
            eventFlowPane.getChildren().add(eventCard);
        });
    }
}
