package com.example.eventlotterysystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * ViewEventActivity displays the details of a selected event and allows the user to join or cancel their participation.
 * Admin users can also delete the event.
 */
public class ViewEventActivity extends AppCompatActivity {

    /** TextView for displaying the event title */
    private TextView eventTitle;

    /** TextView for displaying the event details */
    private TextView eventDetail;

    /** ImageView for displaying the event poster */
    private ImageView eventPoster;

    /** Button for joining or canceling participation in the event */
    private Button joinbutton;

    /** ImageView for deleting the event, visible only to admin users */
    private ImageView deleteButton;

    /** ImageView for returning to the previous activity */
    private ImageView returnButton;

    /** The current event being viewed */
    private Event curEvent;

    /** The currently logged-in user */
    private User curUser;

    /**
     * Called when the activity is first created. Initializes the view elements, retrieves the
     * Event object and user details, and sets up the UI with event data.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_entrant_join);

        curUser = Control.getCurrentUser();

        // Initialize views
        eventTitle = findViewById(R.id.name);
        eventDetail = findViewById(R.id.Event_detail);
        eventPoster = findViewById(R.id.poster);
        deleteButton = findViewById(R.id.del_button);
        returnButton = findViewById(R.id.return_button);
        joinbutton = findViewById(R.id.Entrant_join_button);

        // Retrieve the Event object passed via intent
        int id = (int) getIntent().getSerializableExtra("eventID");
        for (Event event : Control.getInstance().getEventList()) {
            if (event.getEventID() == id) {
                curEvent = event;
                break;
            }
        }

        // Set text for join button based on user's enrollment status
        if (curEvent != null && curUser != null) {
            boolean enrolled = false;
            for (Event event : curUser.getEnrolledList()) {
                if (event.getEventID() == curEvent.getEventID()) {
                    enrolled = true;
                    break;
                }
            }
            joinbutton.setText(enrolled ? "Cancel Event" : "Join Event");
        }

        // Hide delete button if user is not an admin
        if (!curUser.isAdmin()) {
            deleteButton.setVisibility(View.GONE);
        }

        // Populate the UI with event data
        if (curEvent != null) {
            eventTitle.setText(curEvent.getName());
            eventDetail.setText("Description: " + curEvent.getDescription() + "\n"
                    + "Capacity of Event: (" + (curEvent.getChosenList().size() + curEvent.getFinalList().size()) + "/" + curEvent.getLimitChosenList() + ")\n"
                    + "Capacity of Waiting List: (" + curEvent.getWaitingList().size() + "/" + curEvent.getLimitWaitinglList() + ")");
        }

        // Set up the return button to go back to the Events list
        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewEventActivity.this, EventslistActivity.class);
            startActivity(intent);
        });

        // Join or cancel participation in the event based on current status
        joinbutton.setOnClickListener(v -> {
            if (joinbutton.getText().equals("Join Event")) {
                Control.getCurrentUser().joinEvent(curEvent);
                joinbutton.setText("Cancel Event");
            } else {
                Control.getCurrentUser().cancelEvent(curEvent);
                joinbutton.setText("Join Event");
            }
            // Save user action to Firestore
            FirestoreManager.getInstance().saveControl(Control.getInstance());
        });
    }
}
