package com.example.eventlotterysystem;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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

    private FusedLocationProviderClient fusedLocationClient;

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
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if ((curEvent.getChosenList().size() + curEvent.getFinalList().size())>=curEvent.getLimitChosenList() || curEvent.getWaitingList().size() >= curEvent.getLimitWaitinglList()){
            joinbutton.setEnabled(false);
        }
        
        // Join or cancel participation in the event based on current status
        joinbutton.setOnClickListener(v -> {
            if (joinbutton.getText().equals("Join Event")) {
                if (curEvent.getGeoSetting()) {
                    // Create a dialog if geolocation is required
                    new android.app.AlertDialog.Builder(ViewEventActivity.this)
                            .setMessage("This event requires geo information. Do you want to join?")
                            .setPositiveButton("Confirm", (dialog, which) -> {
                                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED
                                        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {

                                    // Request location permissions from the user
                                    ActivityCompat.requestPermissions(this,
                                            new String[]{
                                                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                                            },
                                            1001); // Request code (unique identifier)
                                    return; // Exit the current method to wait for the user's response
                                }

                                // Permission is already granted; proceed with location access
                                fusedLocationClient.getLastLocation()
                                        .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Location> task) {
                                                if (task.isSuccessful() && task.getResult() != null) {
                                                    // Location found, now get the latitude and longitude
                                                    Location location = task.getResult();
                                                    double latitude = location.getLatitude();
                                                    double longitude = location.getLongitude();
                                                    // Store geo-location in Firestore
                                                    curEvent.getLatitudeList().add(latitude);
                                                    curEvent.getLongitudeList().add(longitude);
                                                    Toast.makeText(getApplicationContext(),
                                                            "Latitude: " + latitude + ", Longitude: " + longitude,
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                // User confirmed, proceed with joining the event
                                Control.getCurrentUser().joinEvent(curEvent);
                                joinbutton.setText("Cancel Event");
                                // Update event details
                                eventDetail.setText("Description: " + curEvent.getDescription() + "\n"
                                        + "Capacity of Event: (" + (curEvent.getChosenList().size() + curEvent.getFinalList().size()) + "/" + curEvent.getLimitChosenList() + ")\n"
                                        + "Capacity of Waiting List: (" + curEvent.getWaitingList().size() + "/" + curEvent.getLimitWaitinglList() + ")");
                                // Save user action to Firestore
                                FirestoreManager.getInstance().saveControl(Control.getInstance());
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                // User canceled, don't join the event
                                dialog.dismiss();
                            })
                            .create()
                            .show();
                } else {
                    // No geo requirement, join the event directly
                    Control.getCurrentUser().joinEvent(curEvent);
                    joinbutton.setText("Cancel Event");

                    // Update event details
                    eventDetail.setText("Description: " + curEvent.getDescription() + "\n"
                            + "Capacity of Event: (" + (curEvent.getChosenList().size() + curEvent.getFinalList().size()) + "/" + curEvent.getLimitChosenList() + ")\n"
                            + "Capacity of Waiting List: (" + curEvent.getWaitingList().size() + "/" + curEvent.getLimitWaitinglList() + ")");

                    // Save user action to Firestore
                    FirestoreManager.getInstance().saveControl(Control.getInstance());
                }
            } else {
                // User clicked to cancel event
                Control.getCurrentUser().cancelEvent(curEvent);
                joinbutton.setText("Join Event");

                // Update event details
                eventDetail.setText("Description: " + curEvent.getDescription() + "\n"
                        + "Capacity of Event: (" + (curEvent.getChosenList().size() + curEvent.getFinalList().size()) + "/" + curEvent.getLimitChosenList() + ")\n"
                        + "Capacity of Waiting List: (" + curEvent.getWaitingList().size() + "/" + curEvent.getLimitWaitinglList() + ")");

                // Save user action to Firestore
                FirestoreManager.getInstance().saveControl(Control.getInstance());
            }
        });
    }
}
