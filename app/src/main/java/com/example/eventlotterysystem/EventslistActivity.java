package com.example.eventlotterysystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EventslistActivity extends AppCompatActivity {

    private LinearLayout enrolledList;
    private LinearLayout ownedList;
    private LinearLayout allList;
    private Control control;
    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_lists_screen);

        control = Control.getInstance();
//        control.setCurrentUser(control.getUserList().get(2));
        curUser = Control.getCurrentUser();

        ImageButton returnButton = findViewById(R.id.return_button);
        // Return button to go back
        returnButton.setOnClickListener(v -> {
            Intent intent = new Intent(EventslistActivity.this, Landing_page.class);
            startActivity(intent);
        });

        enrolledList = findViewById(R.id.enrolledList);
        ownedList = findViewById(R.id.ownedList);
        allList = findViewById(R.id.allList);

        // Populate enrolled, owned, and all events lists
        for (Event event : curUser.getEnrolledList()) {
            addEventToSection(event, enrolledList);
        }

        for (Event event : curUser.getOrganizedList()) {
            addEventToSection(event, ownedList);
        }

        for (Event event : control.getEventList()) {
            addEventToSection(event, allList);
        }

        Button createEventButton = findViewById(R.id.create_button);
        createEventButton.setOnClickListener(v -> {
            // Check if the user has a facility
            if (curUser.getFacility() == null) {
                // Display an AlertDialog to inform the user
                new AlertDialog.Builder(this)
                        .setTitle("Facility Required")
                        .setMessage("You need to have a facility first.")
                        .setPositiveButton("Confirm", (dialog, which) -> {
                            // Go back to the previous screen
//                            finish();
                        })
                        .show();
            } else {
                // Proceed with the event creation dialog if the user has a facility
                CreateEventDialogFragment dialog = new CreateEventDialogFragment();
                dialog.setCreateEventListener(newEvent -> {
                    // Add the new event to the control's event list
                    control.getEventList().add(newEvent);
                    curUser.getOrganizedList().add(newEvent);

                    // Refresh the events list on the screen
                    addEventToSection(newEvent, allList);
                    addEventToSection(newEvent, ownedList);
                    FirestoreManager.getInstance().saveControl(Control.getInstance());
                });
                dialog.show(getSupportFragmentManager(), "CreateEventDialogFragment");
            }
        });

    }

    /**
     * Adds an event to the specified section layout and sets up the click listener
     * for viewing the event details in ViewEventActivity.
     *
     * @param event   The event to display
     * @param section The section (enrolled, owned, or all) to add the event to
     */
    private void addEventToSection(Event event, LinearLayout section) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View eventView = inflater.inflate(R.layout.event_content, section, false);

        TextView nameTextView = eventView.findViewById(R.id.name);
        TextView statusTextView = eventView.findViewById(R.id.user_status);
        nameTextView.setText(event.getName());
        statusTextView.setText(event.getDescription());

        // Set click listener to open ViewEventActivity with the event details
        eventView.setOnClickListener(v -> {
            boolean manage = false;
            for (Event orgEvent : curUser.getOrganizedList()) {
                if (orgEvent.getEventID() == event.getEventID()){
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

        // Set up edit and delete buttons
        ImageButton editButton = eventView.findViewById(R.id.button1);
        ImageButton deleteButton = eventView.findViewById(R.id.button2);
        editButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);

//        editButton.setOnClickListener(v -> {
//        });
//
//        deleteButton.setOnClickListener(v -> {
//            section.removeView(eventView);
//        });

        section.addView(eventView);
    }
}
