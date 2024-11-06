package com.example.eventlotterysystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

/**
 * Activity that displays enrolled and owned events.
 * A button that navigates to AllEventsActivity is also available.
 * Event Creation Button is available to the Organizer role
 */
public class EventslistActivity extends AppCompatActivity {

    private LinearLayout enrolledList;
    private LinearLayout ownedList;
    private LinearLayout allList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_lists_screen);

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(view -> finish());

        enrolledList = findViewById(R.id.enrolledList);
        ownedList = findViewById(R.id.ownedList);
        allList = findViewById(R.id.allList);

        // Example data to populate lists
        addEventToSection("Enrolled Event 1", "Event information", enrolledList);
        addEventToSection("Owned Event 1", "Event information", ownedList);
        addEventToSection("All Event 1", "Event information", allList);

        Button createEventButton = findViewById(R.id.create_button);
        createEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(EventslistActivity.this, CreateEventActivity.class);
            startActivity(intent);
        });

        Button allEventsButton = findViewById(R.id.all_events_button);
        allEventsButton.setOnClickListener(v -> {
            Intent intent = new Intent(EventslistActivity.this, AllEventsActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Method that helps display events along with Edit and Delete Buttons for each.
     * Working with mock data so editing and deleting is not done from this activity yet.
     * @param eventName The name of the event
     * @param eventInfo The event's description
     * @param section The section (enrolled, owned) to add the event to
     */
    private void addEventToSection(String eventName, String eventInfo, LinearLayout section) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View eventView = inflater.inflate(R.layout.event_content, section, false);

        // Populate event content
        TextView nameTextView = eventView.findViewById(R.id.name);
        TextView statusTextView = eventView.findViewById(R.id.user_status);
        nameTextView.setText(eventName);
        statusTextView.setText(eventInfo);

        // Setup buttons
        ImageButton editButton = eventView.findViewById(R.id.button1);
        ImageButton deleteButton = eventView.findViewById(R.id.button2);

        // Set click listeners for buttons (customize as needed)
        editButton.setOnClickListener(v -> {
            // Code to edit event
        });

        deleteButton.setOnClickListener(v -> {
            // Code to delete event
            section.removeView(eventView); // Removes the event from the section
        });

        // Add the event view to the specified section
        section.addView(eventView);
    }
}
