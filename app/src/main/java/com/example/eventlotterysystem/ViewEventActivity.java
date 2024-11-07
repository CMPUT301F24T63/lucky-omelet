package com.example.eventlotterysystem;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ViewEventActivity extends AppCompatActivity {

    private TextView eventTitle;
    private TextView eventDetail;
    private ImageView eventPoster;
    private Button joinbutton;
    private ImageView deleteButton;
    private ImageView returnButton;
    private Event curEvent;
    private Control control;
    private User curUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_entrant_join);  // Using the provided layout

        control = Control.getInstance();
        curUser = control.getCurrentUser();
//        Toast.makeText(this, "Event clicked", Toast.LENGTH_SHORT).show();
        // Initialize views
        eventTitle = findViewById(R.id.name);
        eventDetail = findViewById(R.id.Event_detail);
        eventPoster = findViewById(R.id.poster);
        deleteButton = findViewById(R.id.del_button);
        returnButton = findViewById(R.id.return_button);
        joinbutton = findViewById(R.id.Entrant_join_button);
        // Retrieve the Event object passed via intent
        int id = (int) getIntent().getSerializableExtra("eventID");
        for (Event event : control.getEventList()) {
            if (event.getEventID() == id){
                curEvent = event;
                break;
            }
        }

        // set text for join button
        if (curEvent != null && curUser != null) {
            boolean enrolled = false;
            for (Event event : curUser.getEnrolledList()) {
                if (event.getEventID() == curEvent.getEventID()) {
                    enrolled = true;
                    break;
                }
            }
            if (enrolled) {
                joinbutton.setText("Cancel Event");
            } else {
                joinbutton.setText("Join Event");
            }
        }

        if (!curUser.isAdmin()){
            deleteButton.setVisibility(View.GONE);
        }
        if (curEvent != null) {
            // Populate the UI with event data
            eventTitle.setText(curEvent.getName());
            eventDetail.setText("Description: " + curEvent.getDescription() + "\n"
                    + "Event Size: (0/" + curEvent.getLimitChosenList() + ")\n"
                    + "Waiting List Size: (" + curEvent.getLimitWaitinglList() + ")");
        }

        // Return button to go back
        returnButton.setOnClickListener(view -> finish());

        joinbutton.setOnClickListener(v -> {
            if (joinbutton.getText().equals("Join Event")) {
                Control.getCurrentUser().joinEvent(curEvent);
                joinbutton.setText("Cancel Event");
            } else {
                Control.getCurrentUser().cancelEvent(curEvent);
                joinbutton.setText("Join Event");
            }
            // Save user action
            FirestoreManager.getInstance().saveControl(Control.getInstance());
        });
    }
}
