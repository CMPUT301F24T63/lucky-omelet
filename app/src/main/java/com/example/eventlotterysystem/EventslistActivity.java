package com.example.eventlotterysystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * EventslistActivity displays a categorized list of events, including organized, waiting, canceled,
 * chosen, final, and other events. Users can create events and view detailed information about each event.
 */
public class EventslistActivity extends AppCompatActivity {

    /** Layout for displaying organized events */
    private LinearLayout orglist;

    /** Layout for displaying events the user is on the waiting list for */
    private LinearLayout waitlist;

    /** Layout for displaying events the user has canceled */
    private LinearLayout cancellist;

    /** Layout for displaying events the user is chosen for */
    private LinearLayout chosenlist;

    /** Layout for displaying events the user is in the final list for */
    private LinearLayout finallist;

    /** Layout for displaying other events */
    private LinearLayout otherlist;

    /** Control instance for accessing events and user information */
    private Control control;

    /** Currently logged-in user */
    private User curUser;

    /**
     * Called when the activity is first created. Initializes the view elements,
     * populates the categorized event lists, and sets up event creation functionality.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_lists_screen);

        control = Control.getInstance();
        curUser = Control.getCurrentUser();

        ImageButton returnButton = findViewById(R.id.return_button);
        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(EventslistActivity.this, Landing_page.class);
            startActivity(intent);
        });

        orglist = findViewById(R.id.org);
        waitlist = findViewById(R.id.wait);
        cancellist = findViewById(R.id.cancel);
        chosenlist = findViewById(R.id.chosen);
        finallist = findViewById(R.id.finall);
        otherlist = findViewById(R.id.other);

        // Populate enrolled, owned, and all events lists
        for (Event event : control.getEventList()) {
            if (event.getCreator().getUserID() == curUser.getUserID()) {
                addEventToSection(event, orglist);
            } else if (inList(event.getWaitingList(), curUser)) {
                addEventToSection(event, waitlist);
            } else if (inList(event.getCancelledList(), curUser)) {
                addEventToSection(event, cancellist);
            } else if (inList(event.getChosenList(), curUser)) {
                addEventToSection(event, chosenlist);
            } else if (inList(event.getFinalList(), curUser)) {
                addEventToSection(event, finallist);
            } else {
                addEventToSection(event, otherlist);
            }
        }

        Button createEventButton = findViewById(R.id.create_button);
        createEventButton.setOnClickListener(v -> {
            if (curUser.getFacility() == null) {
                // Display an AlertDialog to inform the user they need a facility
                new AlertDialog.Builder(this)
                        .setTitle("Facility Required")
                        .setMessage("You need to have a facility first.")
                        .setPositiveButton("Confirm", (dialog, which) -> {})
                        .show();
            } else {
                // Show event creation dialog if user has a facility
                CreateEventDialogFragment dialog = new CreateEventDialogFragment();
                dialog.setCreateEventListener(newEvent -> {
                    control.getEventList().add(newEvent);
                    curUser.getOrganizedList().add(newEvent);
                    addEventToSection(newEvent, orglist);
                    FirestoreManager.getInstance().saveControl(Control.getInstance());
                });
                dialog.show(getSupportFragmentManager(), "CreateEventDialogFragment");
            }
        });
    }

    /**
     * Checks if the specified user is in the given list.
     *
     * @param l The list of users to check
     * @param u The user to find
     * @return True if the user is in the list, false otherwise
     */
    private boolean inList(ArrayList<User> l, User u) {
        for (User user : l) {
            if (user.getUserID() == u.getUserID()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds an event to the specified section layout and sets up the click listener
     * for viewing the event details in ViewEventActivity.
     *
     * @param event   The event to display
     * @param section The section (organized, waiting, canceled, etc.) to add the event to
     */
    private void addEventToSection(Event event, LinearLayout section) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View eventView = inflater.inflate(R.layout.event_content, section, false);

        TextView nameTextView = eventView.findViewById(R.id.name);
        TextView statusTextView = eventView.findViewById(R.id.user_status);
        nameTextView.setText(event.getName());
        statusTextView.setText(event.getDescription());

        // Set click listener to open the appropriate activity with event details
        eventView.setOnClickListener(v -> {
            boolean manage = false;
            for (Event orgEvent : curUser.getOrganizedList()) {
                if (orgEvent.getEventID() == event.getEventID()) {
                    manage = true;
                    break;
                }
            }
            Intent intent = manage
                    ? new Intent(this, ManageEventActivity.class)
                    : new Intent(this, ViewEventActivity.class);
            intent.putExtra("eventID", event.getEventID());
            startActivity(intent);
        });

        // Set up edit and delete buttons (initially invisible)
        ImageButton editButton = eventView.findViewById(R.id.button1);
        ImageButton deleteButton = eventView.findViewById(R.id.button2);
        editButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);

        section.addView(eventView);
    }
}
